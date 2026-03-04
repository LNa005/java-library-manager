import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO {

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:biblioteca.db");
    }

    public void inicializarSeguridad() {
        String sqlCrearTabla = "CREATE TABLE IF NOT EXISTS usuarios (" +
                               "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                               "usuario TEXT UNIQUE NOT NULL, " +
                               "password TEXT NOT NULL" +
                               ");";
        
        String sqlVerificarAdmin = "SELECT COUNT(*) AS total FROM usuarios WHERE usuario = 'admin'";
        String sqlInsertarAdmin = "INSERT INTO usuarios (usuario, password) VALUES (?, ?)";

        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            // 1. Crear la tabla si no existe
            stmt.execute(sqlCrearTabla);
            
            // 2. Comprobar si existe el administrador
            try (ResultSet rs = stmt.executeQuery(sqlVerificarAdmin)) {
                if (rs.next() && rs.getInt("total") == 0) {
                    // 3. Generar el Hash usando BCrypt
                    String adminHash = BCrypt.hashpw("1234", BCrypt.gensalt());
                    
                    // 4. Insertar usando PreparedStatement para evitar inyecciones SQL
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlInsertarAdmin)) {
                        pstmt.setString(1, "admin");
                        pstmt.setString(2, adminHash);
                        pstmt.executeUpdate();
                        System.out.println("Usuario administrador creado por defecto con hash.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en la inicialización de seguridad: " + e.getMessage());
        }
    }

    public boolean validarLogin(String usuario, String passwordPlano) {
        // Solo se recupera el hash, no se filtra por contraseña en SQL
        String sql = "SELECT password FROM usuarios WHERE usuario = ?";
        
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String hashRecuperado = rs.getString("password");
                    // Se verifica matemáticamente si la contraseña plana corresponde al hash
                    return BCrypt.checkpw(passwordPlano, hashRecuperado); 
                }
                return false; // Usuario no encontrado
            }
        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
            return false;
        }
    }
}
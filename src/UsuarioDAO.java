import java.sql.*;

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
        String sqlInsertarAdmin = "INSERT INTO usuarios (usuario, password) VALUES ('admin', '1234')";

        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            // 1. Crear la tabla si no existe
            stmt.execute(sqlCrearTabla);
            
            // 2. Comprobar si existe el administrador
            ResultSet rs = stmt.executeQuery(sqlVerificarAdmin);
            if (rs.next() && rs.getInt("total") == 0) {
                // 3. Insertar el admin por defecto solo si la tabla está vacía
                stmt.execute(sqlInsertarAdmin);
                System.out.println("✅ Usuario administrador creado por defecto.");
            }
        } catch (SQLException e) {
            System.err.println("Error en la inicialización de seguridad: " + e.getMessage());
        }
    }

    public boolean validarLogin(String usuario, String password) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND password = ?";
        
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            pstmt.setString(2, password); // NOTA: En un entorno de producción, esto debe estar encriptado (Hash).
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Retorna true si encuentra una coincidencia exacta
            }
        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
            return false;
        }
    }
} 
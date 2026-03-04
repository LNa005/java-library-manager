import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class LibroDAO {

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:biblioteca.db");
    }

    public void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS libros (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "titulo TEXT NOT NULL," +
                     "autor TEXT NOT NULL," +
                     "prestado INTEGER DEFAULT 0," +
                     "fecha_prestamo TEXT" +
                     ");";
        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error al crear tabla: " + e.getMessage());
        }
    }

    public ArrayList<Libro> obtenerTodos() {
        ArrayList<Libro> lista = new ArrayList<>();
        // Corrección: Extracción explícita de la clave primaria 'id'
        String sql = "SELECT id, titulo, autor, prestado, fecha_prestamo FROM libros";
        
        try (Connection conn = conectar(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Libro libro = new Libro(rs.getString("titulo"), rs.getString("autor"));
                libro.setId(rs.getInt("id")); // Mapeo de la clave primaria al objeto
                
                if (rs.getInt("prestado") == 1) {
                    libro.prestar();
                    String fecha = rs.getString("fecha_prestamo");
                    // Corrección: Evaluación contra un null real de base de datos
                    if (fecha != null) { 
                        libro.setFechaPrestamo(LocalDate.parse(fecha));
                    }
                }
                lista.add(libro);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener libros: " + e.getMessage());
        }
        return lista;
    }

    public void insertar(Libro libro) {
        String sql = "INSERT INTO libros(titulo, autor, prestado, fecha_prestamo) VALUES(?,?,?,?)";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, libro.getTitulo());
            pstmt.setString(2, libro.getAutor());
            pstmt.setInt(3, libro.estaPrestado() ? 1 : 0);
            
            // Corrección: Inserción de tipo NULL nativo
            if (libro.getFechaPrestamo() == null) {
                pstmt.setNull(4, Types.VARCHAR);
            } else {
                pstmt.setString(4, libro.getFechaPrestamo().toString());
            }
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar libro: " + e.getMessage());
        }
    }

    public void actualizarEstado(Libro libro) {
        // Corrección: Condición WHERE fijada al identificador único
        String sql = "UPDATE libros SET prestado = ?, fecha_prestamo = ? WHERE id = ?";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, libro.estaPrestado() ? 1 : 0);
            
            if (libro.getFechaPrestamo() == null) {
                pstmt.setNull(2, Types.VARCHAR);
            } else {
                pstmt.setString(2, libro.getFechaPrestamo().toString());
            }
            
            pstmt.setInt(3, libro.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado: " + e.getMessage());
        }
    }

    public void eliminar(int id) {
        // Corrección: Eliminación precisa mediante clave primaria
        String sql = "DELETE FROM libros WHERE id = ?";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
        }
    }
}
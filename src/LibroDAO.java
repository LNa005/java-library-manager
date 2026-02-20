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
        String sql = "SELECT titulo, autor, prestado, fecha_prestamo FROM libros";
        try (Connection conn = conectar(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Libro libro = new Libro(rs.getString("titulo"), rs.getString("autor"));
                if (rs.getInt("prestado") == 1) {
                    libro.prestar();
                    String fecha = rs.getString("fecha_prestamo");
                    if (fecha != null && !fecha.equals("null")) {
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
            pstmt.setString(4, libro.getFechaPrestamo() == null ? "null" : libro.getFechaPrestamo().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar libro: " + e.getMessage());
        }
    }

    public void actualizarEstado(Libro libro) {
        String sql = "UPDATE libros SET prestado = ?, fecha_prestamo = ? WHERE titulo = ?";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, libro.estaPrestado() ? 1 : 0);
            pstmt.setString(2, libro.getFechaPrestamo() == null ? "null" : libro.getFechaPrestamo().toString());
            pstmt.setString(3, libro.getTitulo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado: " + e.getMessage());
        }
    }

    public void eliminar(String titulo) {
        String sql = "DELETE FROM libros WHERE titulo = ?";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, titulo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
        }
    }
}
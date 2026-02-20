import java.time.LocalDate; // Importante para manejar fechas

public class Libro {
    private String titulo;
    private String autor;
    private boolean isPrestado;
    private LocalDate fechaPrestamo; // NUEVO CAMPO

    public Libro(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
        this.isPrestado = false;
        this.fechaPrestamo = null; // Al principio no tiene fecha
    }

    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public boolean estaPrestado() { return isPrestado; }
    
    // Getter nuevo para la fecha
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }

    // Al prestar, guardamos la fecha de HOY
    public void prestar() { 
        this.isPrestado = true;
        this.fechaPrestamo = LocalDate.now(); // Pone la fecha actual del sistema
    }

    // Al devolver, borramos la fecha
    public void devolver() { 
        this.isPrestado = false;
        this.fechaPrestamo = null;
    }
    
    // Si queremos forzar una fecha (útil para cargar datos)
    public void setFechaPrestamo(LocalDate fecha) {
        this.fechaPrestamo = fecha;
    }

    @Override
    public String toString() {
        String estado = isPrestado ? "[PRESTADO el " + fechaPrestamo + "]" : "[DISPONIBLE]";
        return estado + " - " + titulo + " (" + autor + ")";
    }
}
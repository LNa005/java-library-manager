import java.time.LocalDate;

public class Libro {
    private int id; // Mapeo de Primary Key
    private String titulo;
    private String autor;
    private boolean isPrestado;
    private LocalDate fechaPrestamo; 

    public Libro(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
        this.isPrestado = false;
        this.fechaPrestamo = null; 
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public boolean estaPrestado() { return isPrestado; }
    
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }

    public void prestar() { 
        this.isPrestado = true;
        this.fechaPrestamo = LocalDate.now(); 
    }

    public void devolver() { 
        this.isPrestado = false;
        this.fechaPrestamo = null;
    }
    
    public void setFechaPrestamo(LocalDate fecha) {
        this.fechaPrestamo = fecha;
    }

    @Override
    public String toString() {
        String estado = isPrestado ? "[PRESTADO el " + fechaPrestamo + "]" : "[DISPONIBLE]";
        return "ID: " + id + " | " + estado + " - " + titulo + " (" + autor + ")";
    }
}
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VentanaBiblioteca extends JFrame {

    private ArrayList<Libro> biblioteca = new ArrayList<>();
    private DefaultTableModel modeloTabla;
    private JTable tablaLibros;
    private JTextField txtTitulo, txtAutor, txtBuscador;
    
    // Instancia de la capa de acceso a datos
    private LibroDAO libroDAO = new LibroDAO();

    public VentanaBiblioteca() {
        // 1. Inicializar la base de datos a través del DAO
        libroDAO.crearTablaSiNoExiste();

        // 2. Estética del sistema operativo
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) { }

        // 3. Configuración de la ventana principal
        setTitle("Gestión Bibliotecaria SQL PRO - v5.2 (Refactorizada)");
        try { setIconImage(new ImageIcon("icono.png").getImage()); } catch (Exception e) {}
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierre directo y seguro
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 4. Montaje de la interfaz
        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("📝 Registro Directo", crearPanelGestion());
        pestañas.addTab("📚 Inventario", crearPanelInventario());
        add(pestañas, BorderLayout.CENTER);

        // 5. Carga inicial
        cargarDatosDesdeBD();
    }

    private void cargarDatosDesdeBD() {
        biblioteca = libroDAO.obtenerTodos();
        actualizarTabla("");
    }

    private JPanel crearPanelGestion() {
        JPanel panel = new JPanel(null);
        JLabel lblHeader = new JLabel("Alta de Libros");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeader.setBounds(30, 20, 400, 30);
        panel.add(lblHeader);

        JLabel lblTitulo = new JLabel("Título:");
        lblTitulo.setBounds(30, 80, 100, 25);
        panel.add(lblTitulo);
        txtTitulo = new JTextField();
        txtTitulo.setBounds(100, 80, 280, 25);
        panel.add(txtTitulo);

        JLabel lblAutor = new JLabel("Autor:");
        lblAutor.setBounds(30, 120, 100, 25);
        panel.add(lblAutor);
        txtAutor = new JTextField();
        txtAutor.setBounds(100, 120, 280, 25);
        panel.add(txtAutor);

        JButton btnGuardar = new JButton("💾 GUARDAR LIBRO");
        btnGuardar.setBounds(100, 170, 280, 45);
        btnGuardar.setBackground(new Color(0, 120, 215));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnGuardar.addActionListener(e -> {
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            
            if (titulo.isEmpty() || autor.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Rellena todos los campos.");
                return;
            }

            for (Libro l : biblioteca) {
                if (l.getTitulo().equalsIgnoreCase(titulo)) {
                    JOptionPane.showMessageDialog(this, "El libro ya existe.");
                    return;
                }
            }

            Libro nuevoLibro = new Libro(titulo, autor);
            biblioteca.add(nuevoLibro);
            
            // Guardado individual delegado al DAO
            libroDAO.insertar(nuevoLibro); 
            
            actualizarTabla(""); 
            txtTitulo.setText("");
            txtAutor.setText("");
            txtTitulo.requestFocus();
        });

        panel.add(btnGuardar);
        return panel;
    }

    private JPanel crearPanelInventario() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscador = new JTextField(20);
        JButton btnBuscar = new JButton("🔍 Filtrar");

        btnBuscar.addActionListener(e -> actualizarTabla(txtBuscador.getText().trim()));
        txtBuscador.addActionListener(e -> btnBuscar.doClick());

        topPanel.add(new JLabel("Buscar:"));
        topPanel.add(txtBuscador);
        topPanel.add(btnBuscar);
        panel.add(topPanel, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"Título", "Autor", "Estado", "Fecha"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaLibros = new JTable(modeloTabla);
        panel.add(new JScrollPane(tablaLibros), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnPrestar = new JButton("📖 Prestar");
        JButton btnDevolver = new JButton("↩️ Devolver");
        JButton btnEliminar = new JButton("🗑 Eliminar");

        btnPrestar.addActionListener(e -> {
            Libro l = obtenerLibroSeleccionado();
            if (l != null && !l.estaPrestado()) { 
                l.prestar(); 
                libroDAO.actualizarEstado(l); // Actualización en tiempo real
                actualizarTabla(txtBuscador.getText()); 
            }
        });

        btnDevolver.addActionListener(e -> {
            Libro l = obtenerLibroSeleccionado();
            if (l != null && l.estaPrestado()) { 
                l.devolver(); 
                libroDAO.actualizarEstado(l); // Actualización en tiempo real
                actualizarTabla(txtBuscador.getText()); 
            }
        });

        btnEliminar.addActionListener(e -> {
            Libro l = obtenerLibroSeleccionado();
            if (l != null) { 
                int resp = JOptionPane.showConfirmDialog(this, "¿Eliminar este libro de la base de datos?");
                if(resp == JOptionPane.YES_OPTION) {
                    biblioteca.remove(l); 
                    libroDAO.eliminar(l.getTitulo()); // Eliminación en tiempo real
                    actualizarTabla(txtBuscador.getText()); 
                }
            }
        });

        panelBotones.add(btnPrestar); panelBotones.add(btnDevolver); panelBotones.add(btnEliminar);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    private void actualizarTabla(String filtro) {
        if (modeloTabla == null) return;
        modeloTabla.setRowCount(0);
        String f = filtro.toLowerCase();
        for (Libro libro : biblioteca) {
            if (filtro.isEmpty() || libro.getTitulo().toLowerCase().contains(f) || libro.getAutor().toLowerCase().contains(f)) {
                modeloTabla.addRow(new Object[]{libro.getTitulo(), libro.getAutor(), 
                    libro.estaPrestado() ? "Prestado" : "Disponible", 
                    libro.getFechaPrestamo() == null ? "" : libro.getFechaPrestamo().toString()});
            }
        }
    }

    private Libro obtenerLibroSeleccionado() {
        int fila = tablaLibros.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un libro de la lista.");
            return null;
        }
        String tit = (String) modeloTabla.getValueAt(fila, 0);
        for (Libro l : biblioteca) if (l.getTitulo().equals(tit)) return l;
        return null;
    }
}
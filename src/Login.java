import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Login() {
        // La inicialización de seguridad se ha movido al método main()
        
        setTitle("Seguridad - Biblioteca v6.0");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        try { setIconImage(new ImageIcon("icono.png").getImage()); } catch (Exception e) {}

        JLabel lblTitulo = new JLabel("ACCESO RESTRINGIDO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(50, 20, 300, 30);
        add(lblTitulo);

        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setBounds(50, 70, 100, 25);
        add(lblUser);

        JTextField txtUser = new JTextField();
        txtUser.setBounds(150, 70, 180, 25);
        add(txtUser);

        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setBounds(50, 110, 100, 25);
        add(lblPass);

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBounds(150, 110, 180, 25);
        add(txtPass);

        JButton btnEntrar = new JButton("🔓 INGRESAR AL SISTEMA");
        btnEntrar.setBounds(80, 170, 240, 40);
        btnEntrar.setBackground(new Color(0, 120, 215));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 14));
        add(btnEntrar);

        btnEntrar.addActionListener(e -> {
            String usuario = txtUser.getText().trim();
            String password = new String(txtPass.getPassword());

            if (usuarioDAO.validarLogin(usuario, password)) {
                new VentanaBiblioteca().setVisible(true);
                dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "❌ Acceso Denegado\nUsuario o contraseña incorrectos.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
                txtPass.setText(""); 
            }
        });
    }

    public static void main(String[] args) {
        // 1. Inicializar la base de datos y la seguridad de forma centralizada
        new UsuarioDAO().inicializarSeguridad();
        
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        
        // 2. Levantar la interfaz gráfica
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
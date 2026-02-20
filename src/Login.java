import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Login() {
        // 1. Inicializar la seguridad (Crea tabla y admin si es la primera vez)
        usuarioDAO.inicializarSeguridad();

        // 2. Configuración de la ventana
        setTitle("Seguridad - Biblioteca v6.0");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        try { setIconImage(new ImageIcon("icono.png").getImage()); } catch (Exception e) {}

        // 3. Interfaz
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

        // 4. Lógica de seguridad delegada al DAO
        btnEntrar.addActionListener(e -> {
            String usuario = txtUser.getText().trim();
            String password = new String(txtPass.getPassword());

            if (usuarioDAO.validarLogin(usuario, password)) {
                new VentanaBiblioteca().setVisible(true);
                dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "❌ Acceso Denegado\nUsuario o contraseña incorrectos.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
                txtPass.setText(""); // Limpiar campo de contraseña por seguridad
            }
        });
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        new Login().setVisible(true);
    }
}
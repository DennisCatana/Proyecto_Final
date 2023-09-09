import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login {
    JPanel Login;
    private JComboBox<String> rolBox;
    private JTextField user;
    private JPasswordField password;
    private JPanel Imagen;
    private JPanel Datos;
    private JButton entrarButton;
    private String seleccionar;
    protected static int idCajeroActual;
    // Configuración de la conexión a la base de datos
    static String DB_URL = "jdbc:mysql://localhost/MEDICAL";
    static String USER = "root";
    static String PASS = "root";
    static String QUERY = "SELECT * FROM Usuario";
    static String veriusu;
    static String vericontra;
    static String selec = "Seleccionar";
    static String usu;
    static String contra;

    public Login() {
        entrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificarConexion();
                JFrame frame;
                boolean hayInformacionUsuario = !user.getText().isEmpty();
                boolean hayInformacionContrasenia = !password.getText().isEmpty();

                // Lógica para determinar qué tipo de frame mostrar según la selección y verificación
                // de usuario y contraseña.

                if (hayInformacionUsuario || hayInformacionContrasenia) {
                    if (selec.equals("Administrador") || selec.equals("Cajero")) {
                        if (selec.equals("Administrador") && selec.equals(seleccionar) && veriusu.equals(user.getText()) && vericontra.equals(new String(password.getPassword()))) {
                            frame = new JFrame("Administrador");
                            frame.setContentPane(new AdminMenu().AdminMenu);
                            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            closeLoginFrame();
                            frame.pack();
                            frame.setSize(1000,500);
                            frame.setLocationRelativeTo(null);
                            frame.setVisible(true);
                        } else if (selec.equals("Cajero") && selec.equals(seleccionar) && veriusu.equals(user.getText()) && vericontra.equals(new String(password.getPassword()))) {
                            frame = new JFrame("Cajero");
                            frame.setContentPane(new CajeroMenu().CajeroMenu);
                            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            closeLoginFrame();
                            frame.pack();
                            frame.setSize(2000, 550);
                            frame.setLocationRelativeTo(null);
                            frame.setVisible(true);
                        }
                    } else if (selec.equals("Seleccionar")) {
                        JOptionPane.showMessageDialog(null,"Usuario o contrseña incorrectos");
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"No has seleccionado un rol");
                    //hayInformacionUsuario = true;
                    //hayInformacionContrasenia = true;
                }

                if (seleccionar.equals("Seleccione") && !hayInformacionUsuario && !hayInformacionContrasenia) {
                    JOptionPane.showMessageDialog(null,"No has ingresado ningun campo y no has seleccionado un rol");
                }
                veriusu = "";
                vericontra = "";
                selec = "Seleccionar";
            }
        });
    }

    // Método para cerrar el JFrame de inicio de sesión
    void closeLoginFrame() {
        JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(Login);
        loginFrame.dispose();}

    // Método para verificar la conexión y autenticar al usuario
    public void verificarConexion() {
        seleccionar = (String) rolBox.getSelectedItem();
        usu = user.getText().trim();
        contra = new String(password.getPassword()).trim();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(QUERY)) {

            while (rs.next()) {
                if (usu.equals(rs.getString("idUsuario"))) {
                    veriusu = rs.getString("idUsuario");
                }
                if (contra.equals(rs.getString("contraseña")) && usu.equals(rs.getString("idUsuario"))) {
                    vericontra = rs.getString("contraseña");
                }
                if (seleccionar.equals(rs.getString("tipoUsuario")) && contra.equals(rs.getString("contraseña")) && usu.equals(rs.getString("idUsuario"))) {
                    selec = rs.getString("tipoUsuario");
                    if (selec.equals("Cajero")) {
                        CajeroMenu.setIdCajeroActual(Integer.parseInt(veriusu));
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Creación y configuración del JFrame de inicio de sesión
        JFrame frame = new JFrame("Login");
        frame.setContentPane(new Login().Login);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(700, 350);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

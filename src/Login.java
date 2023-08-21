import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {

    private JPanel Login;
    private JComboBox rolBox;
    private JTextField user;
    private JPasswordField password;
    private JPanel Imagen;
    private JPanel Datos;
    private JButton entrarButton;


    public Login() {
        // Para que al momento de iniciar el Form no aparezca señalado ningun botón
        Login.setFocusable(true);
        Login.requestFocusInWindow();

        // Lógica para el inicio de sesión
        entrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setContentPane(new Login().Login);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 350);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}


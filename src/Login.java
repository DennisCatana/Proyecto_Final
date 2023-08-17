import javax.swing.*;

public class Login {

    private JPanel Login;
    private JComboBox rolBox;
    private JTextField user;
    private JPasswordField password;
    private JPanel Imagen;
    private JPanel Datos;
    private JButton entrarButton;


    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setContentPane(new Login().Login);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 350);
        frame.setVisible(true);
    }
}


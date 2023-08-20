import javax.swing.*;

public class AdminMenu {
    private JPanel AdminMenu;
    private JPanel Opciones;
    private JButton ventasButton;
    private JButton cajerosButton;
    private JButton cerrarBt;
    private JButton productosButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Administrador - Menu Principal");
        frame.setContentPane(new AdminMenu().AdminMenu);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}

import javax.swing.*;

public class AdminMenu {
    private JPanel AdminMenu;
    private JPanel Opciones;
    private JButton ventasButton;
    private JButton cerrarBt;
    private JButton productosButton;
    private JButton cajerosButton;
    private JPanel OP_Productos;
    private JPanel OP_Ventas;
    private JPanel OP_Cajeros;

    public AdminMenu() {
        // Para que al momento de iniciar el Form no aparezca señalado ningun botón
        AdminMenu.setFocusable(true);
        AdminMenu.requestFocusInWindow();

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Administrador - Menu Principal");
        frame.setContentPane(new AdminMenu().AdminMenu);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}
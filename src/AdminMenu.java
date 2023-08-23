import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminMenu {
    JPanel AdminMenu;
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

        productosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame;
                frame = new JFrame("Productos");
                frame.setContentPane(new Productos().Productos);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                closeAdminMenuFrame();
                frame.pack();
                frame.setSize(1000, 500);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
        ventasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame;
                frame = new JFrame("Ventas");
                frame.setContentPane(new Ventas().Ventas);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                closeAdminMenuFrame();
                frame.pack();
                frame.setSize(1000, 500);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
        cajerosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame;
                frame = new JFrame("Cajeros");
                frame.setContentPane(new Cajeros().Cajeros);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                closeAdminMenuFrame();
                frame.pack();
                frame.setSize(1000, 500);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
        cerrarBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame;
                frame = new JFrame("Login");
                frame.setContentPane(new Login().Login);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                closeAdminMenuFrame();
                frame.pack();
                frame.setSize(1000, 500);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
    private void closeAdminMenuFrame() {
        JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(AdminMenu);
        loginFrame.dispose();}
    public static void main(String[] args) {
        JFrame frame = new JFrame("Administrador - Menu Principal");
        frame.setContentPane(new AdminMenu().AdminMenu);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}

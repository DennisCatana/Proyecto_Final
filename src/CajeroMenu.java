import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CajeroMenu {

     JPanel CajeroMenu;
    private JButton cerrarBt;
    private JPanel Opciones;

    public CajeroMenu() {
        cerrarBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame;
                frame = new JFrame("Login");
                frame.setContentPane(new Login().Login);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                closeCajeroMenuFrame();
                frame.pack();
                frame.setSize(1000, 500);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
    private void closeCajeroMenuFrame() {
        JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(CajeroMenu);
        loginFrame.dispose();}
    public static void main(String[] args) {
        JFrame frame = new JFrame("Cajero - Menú Principal");
        frame.setContentPane(new CajeroMenu().CajeroMenu);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

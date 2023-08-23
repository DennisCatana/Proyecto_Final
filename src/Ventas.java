import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ventas {
     JPanel Ventas;
    private JButton regresarButton;
    public Ventas() {
        regresarButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame;
            frame = new JFrame("AdminMenu");
            frame.setContentPane(new AdminMenu().AdminMenu);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            closeVentasFrame();
            frame.pack();
            frame.setSize(1000, 500);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    });
}

    private void closeVentasFrame() {
        JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(Ventas);
        loginFrame.dispose();
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Ventas");
        frame.setContentPane(new Ventas().Ventas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

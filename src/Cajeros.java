import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Cajeros {
    JPanel Cajeros;
    private JButton regresarButton;

public Cajeros(){
    regresarButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame;
            frame = new JFrame("AdminMenu");
            frame.setContentPane(new AdminMenu().AdminMenu);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            closeCajerosFrame();
            frame.pack();
            frame.setSize(1000, 500);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    });
}
    private void closeCajerosFrame(){
        JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(Cajeros);
        loginFrame.dispose();
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Cajeros");
        frame.setContentPane(new Cajeros().Cajeros);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

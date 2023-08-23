import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Productos {
     JPanel Productos;
    private JButton regresarButton;


    public Productos() {
        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void closeProductosFrame() {
        JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(Productos);
        loginFrame.dispose();}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Productos");
        frame.setContentPane(new Productos().Productos);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

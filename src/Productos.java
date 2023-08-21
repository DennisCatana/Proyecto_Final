import javax.swing.*;

public class Productos {
    private JPanel Productos;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Productos");
        frame.setContentPane(new Productos().Productos);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

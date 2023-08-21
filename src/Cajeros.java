import javax.swing.*;

public class Cajeros {
    private JPanel Cajeros;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cajeros");
        frame.setContentPane(new Cajeros().Cajeros);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

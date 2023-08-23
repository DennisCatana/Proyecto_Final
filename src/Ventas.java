import javax.swing.*;

public class Ventas {
     JPanel Ventas;
    private void closeVentasFrame() {
        JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(Ventas);
        loginFrame.dispose();}
    public static void main(String[] args) {
        JFrame frame = new JFrame("Ventas");
        frame.setContentPane(new Ventas().Ventas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

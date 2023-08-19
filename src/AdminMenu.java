import javax.swing.*;

public class AdminMenu {
    private JPanel AdminMenu;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Menu Principal");
        frame.setContentPane(new AdminMenu().AdminMenu);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 550);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

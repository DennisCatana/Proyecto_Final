import javax.swing.*;

public class CajeroMenu {

    private JPanel CajeroMenu;
    private JButton cerrarBt;
    private JPanel Opciones;
    private JButton busquedaButton;
    private JTextField busqueda;
    private JTable Nombre;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cajero - Menú Principal");
        frame.setContentPane(new CajeroMenu().CajeroMenu);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

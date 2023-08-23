import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

public class CajeroMenu {

    JPanel CajeroMenu;
    private JButton cerrarBt;
    private JPanel Opciones;
    private JButton busquedaButton;
    private JTextField busqueda;
    private JTable Factura;

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
                frame.setSize(700, 350);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
        }
    });
        // Creación de los datos de la tabla
        DefaultTableModel model = new DefaultTableModel(
            // Contenido de la Tabla
            new Object[][] {
                {"1", "Producto 1", "10.00"},
                {"2", "Producto 2", "15.00"},
                {"3", "Producto 3", "20.00"}
            },
            // Nombrar las columnas
            new String[]{"ID","Nombre","Precio"});
        // Poner el modelo hecho en el Jtable
        Factura.setModel(model);
    }
    private void closeCajeroMenuFrame() {
        JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(CajeroMenu);
        loginFrame.dispose();
    }
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
        Factura = new JTable();
    }


}

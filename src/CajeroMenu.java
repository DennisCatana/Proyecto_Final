import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class CajeroMenu {

    JPanel CajeroMenu;
    private JButton cerrarBt;
    private JPanel Opciones;
    private JTable Factura;
    private JTextField cantidad;
    private JButton seleccionarButton;
    private JLabel nomProd;
    private JTextField busqueda;
    private JButton buscarButton;
    private JButton borrarButton;

    // Configuración de la conexión a la base de datos
    static String DB_URL = "jdbc:mysql://localhost/medical";
    static String USER = "root";
    static String PASS = "root";
    static String QUERY = "SELECT * FROM Usuario";

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
            },
            // Nombrar las columnas
            new String[]{"Codigo","Cantidad","Descripción","Valor Unitario", "Valor Total"});
        // Poner el modelo hecho en el Jtable
        Factura.setModel(model);

        // Botón del buscar
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigoProducto = busqueda.getText(); // Obtener el código del producto
                // Realizar la búsqueda en la base de datos
                String nombreProductoEncontrado = buscarNombreProducto(codigoProducto);
                // Actualizar el texto de la etiqueta con el nombre del producto encontrado.
                nomProd.setText(nombreProductoEncontrado);
            }
        });

        // Botón del seleccionar
        seleccionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigoProducto = busqueda.getText(); // Obtener el código del producto
                int cantidadSeleccionada = Integer.parseInt(cantidad.getText()); // Obtener la cantidad

                String nombreProducto = buscarNombreProducto(codigoProducto);
                double valorUnitario = obtenerValorUnitario(codigoProducto);
                double valorTotal = valorUnitario * cantidadSeleccionada;

                DefaultTableModel model = (DefaultTableModel) Factura.getModel();
                model.addRow(new Object[]{codigoProducto, cantidadSeleccionada, nombreProducto, valorUnitario, valorTotal});
            }
        });

        // Para Borrar una fila de la tabla
        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = Factura.getSelectedRow();
                if (selectedRow != -1) {
                    DefaultTableModel model = (DefaultTableModel) Factura.getModel();
                    model.removeRow(selectedRow);
                }
            }
        });
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

    // Función para Buscar productos
    private String buscarNombreProducto(String codigoProducto) {
        String nombreProducto = "Producto no encontrado"; // Valor por defecto

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String QueryBuscar = "SELECT nombreProducto FROM Producto WHERE idProducto = ?";
            try (PreparedStatement statement = connection.prepareStatement(QueryBuscar)) {
                statement.setString(1, codigoProducto);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        nombreProducto = resultSet.getString("nombreProducto");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return nombreProducto;
    }

    // Para encontrar el precio o valor unitario de cada producto
    private double obtenerValorUnitario(String codigoProducto) {
        double valorUnitario = 0.0;

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT precio FROM Producto WHERE idProducto = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, codigoProducto);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        valorUnitario = resultSet.getDouble("precio");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return valorUnitario;
    }

}

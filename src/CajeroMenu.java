import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfAction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.io.FileOutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList; // Importar la clase ArrayList
import java.util.List; // Importar la interfaz List


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
    private JPanel DatosEmpresa;
    private JPanel Comandos;
    private JPanel Venta;
    private JPanel DatosCliente;
    private JTextField nomCli;
    private JTextField idCli;
    private JTextField dirCli;
    private JTable Total;
    private JButton imprimirFac;

    // Configuración de la conexión a la base de datos
    static String DB_URL = "jdbc:mysql://localhost/MEDICAL";
    static String USER = "root";
    static String PASS = "root_bas3";
    static String QUERY = "SELECT * FROM Usuario";

    //Iterador para el numero de factura
    private int numeroFactura = 1; // Inicializar el contador de factura
    private List<String> nombresArchivosPDF = new ArrayList<>(); // Para almacenar los nombres de los archivos PDF


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

        // Creación de los datos de la tabla de Total
        DefaultTableModel totalModel = new DefaultTableModel(
                new Object[][] {
                        {"Subtotal", ""},
                        {"IVA (12%)", ""},
                        {"Total", ""}
                },
                new String[]{"", ""}); // Las columnas no necesitan nombres

        Total.setModel(totalModel);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        Total.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);

        Factura.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
                String codigoProducto = busqueda.getText();
                int cantidadSeleccionada = Integer.parseInt(cantidad.getText());

                String nombreProducto = buscarNombreProducto(codigoProducto);
                double valorUnitario = obtenerValorUnitario(codigoProducto);
                double valorTotal = valorUnitario * cantidadSeleccionada;

                DefaultTableModel model = (DefaultTableModel) Factura.getModel();
                model.addRow(new Object[]{codigoProducto, cantidadSeleccionada, nombreProducto, valorUnitario, valorTotal});

                calcularTotal(); // Llamar a la función para actualizar el resumen
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

                    calcularTotal(); // Llamar a la función para actualizar el resumen
                }
            }
        });
        imprimirFac.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imprimirFactura();

            }
        });
    }
    private void imprimirFactura() {
        Document document = new Document();
        String nombreArchivoPDF = "Nota de Venta " + numeroFactura + ".pdf"; // Generar el nombre del archivo con el número de factura
        try {
            PdfWriter.getInstance(document, new FileOutputStream(nombreArchivoPDF));
            document.open();

            // Información de la factura
            document.add(new Paragraph("Nota de Venta Nº " + numeroFactura ));
            document.add(new Paragraph("Fecha: " + new java.util.Date()+"\n"));
            document.add(new Paragraph("Cliente: " + nomCli.getText()+"\n"));
            document.add(new Paragraph("Direccion: " + dirCli.getText()+"\n\n"));

            // Detalles de los productos en la factura
            DefaultTableModel detalleModel = (DefaultTableModel) Factura.getModel();
            PdfPTable table = new PdfPTable(5); // 5 columnas para: Código, Cantidad, Descripción, Valor Unitario, Valor Total

            for (int i = 0; i < detalleModel.getRowCount(); i++) {
                String codigo = detalleModel.getValueAt(i, 0).toString();
                String cantidad = detalleModel.getValueAt(i, 1).toString();
                String descripcion = detalleModel.getValueAt(i, 2).toString();
                String valorUnitario = detalleModel.getValueAt(i, 3).toString();
                String valorTotal = detalleModel.getValueAt(i, 4).toString();

                table.addCell(codigo);
                table.addCell(cantidad);
                table.addCell(descripcion);
                table.addCell(valorUnitario);
                table.addCell(valorTotal);
            }
            document.add(table);

            // Resumen del total
            DefaultTableModel totalModel = (DefaultTableModel) Total.getModel();
            Paragraph subtotal = new Paragraph("Subtotal: " + totalModel.getValueAt(0, 1));
            subtotal.setAlignment(Element.ALIGN_RIGHT);
            document.add(subtotal);

            Paragraph iva = new Paragraph("IVA (12%): " + totalModel.getValueAt(1, 1));
            iva.setAlignment(Element.ALIGN_RIGHT);
            document.add(iva);

            Paragraph total = new Paragraph("Total: " + totalModel.getValueAt(2, 1));
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();
            JOptionPane.showMessageDialog(null, "Nota de venta generada exitosamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            nombresArchivosPDF.add(nombreArchivoPDF); // Agregar el nombre del archivo a la lista
            numeroFactura++; // Incrementar el número de factura
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void closeCajeroMenuFrame() {
        JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(CajeroMenu);
        loginFrame.dispose();
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

    private void calcularTotal() {
        DefaultTableModel detalleModel = (DefaultTableModel) Factura.getModel();
        DefaultTableModel totalModel = (DefaultTableModel) Total.getModel();

        BigDecimal subtotal = BigDecimal.ZERO;
        for (int i = 0; i < detalleModel.getRowCount(); i++) {
            BigDecimal valorUnitario = new BigDecimal(detalleModel.getValueAt(i, 3).toString());
            BigDecimal cantidad = new BigDecimal(detalleModel.getValueAt(i, 1).toString());
            BigDecimal subTotalFila = valorUnitario.multiply(cantidad);
            detalleModel.setValueAt(subTotalFila, i, 4);
            subtotal = subtotal.add(subTotalFila);
        }

        BigDecimal iva = subtotal.multiply(new BigDecimal("0.12"));
        BigDecimal total = subtotal.add(iva);

        DecimalFormat formatoDecimal = new DecimalFormat("#.00"); // Formato para que sea de dos decimales
        String formatoSubtotal = formatoDecimal.format(subtotal);
        String formatoIva = formatoDecimal.format(iva);
        String formatoTotal = formatoDecimal.format(total);

        totalModel.setValueAt(formatoSubtotal, 0, 1); // Actualizar el valor del Subtotal en la tabla
        totalModel.setValueAt(formatoIva, 1, 1);       // Actualizar el valor del IVA en la tabla
        totalModel.setValueAt(formatoTotal, 2, 1);     // Actualizar el valor del Total en la tabla
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
        Total = new JTable();
    }
}

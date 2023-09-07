import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.*;
public class CajeroMenu {

    JPanel CajeroMenu;
    private JButton cerrarBt;
    private JPanel Opciones;
    private JTable Factura;
    private JTextField cantidad;
    private JButton seleccionarButton;
    private JTextField nomProd;
    private JTextField busqueda;
    private JButton buscarButton;
    private JButton borrarButton;
    private JPanel Comandos;
    private JPanel Venta;
    private JPanel DatosCliente;
    private JTextField nomCli;
    private JTextField idCli;
    private JTextField dirCli;
    private JTable Total;
    private JButton imprimirFac;
    private JLabel ruc;
    private JLabel sucursal;
    private JLabel adress;
    private JLabel title;
    private JTable products;
    protected static int idCajeroActual;
    private int numeroNotaVenta = -1;
    private int fac;

    // Configuración de la conexión a la base de datos
    static String DB_URL = "jdbc:mysql://localhost/MEDICAL";
    static String USER = "root";
    static String PASS = "root";
    static String QUERY = "SELECT * FROM Usuario";

    //Iterador para el numero de factura
    private int numeroFactura = 1; // Inicializar el contador de factura
    private List<String> nombresArchivosPDF = new ArrayList<>(); // Para almacenar los nombres de los archivos PDF


    public CajeroMenu() {
        mostrarproductos();
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
                String codigoProducto = nomProd.getText(); // Obtener el código del producto
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
                busqueda.setText("");
                cantidad.setText("");
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
                DefaultTableModel totalModel = (DefaultTableModel) Total.getModel();
                String subtotalStr = totalModel.getValueAt(0, 1).toString();
                String ivaStr = totalModel.getValueAt(1, 1).toString();
                String totalStr = totalModel.getValueAt(2, 1).toString();

                BigDecimal subtotal = new BigDecimal(subtotalStr);
                BigDecimal iva = new BigDecimal(ivaStr);
                BigDecimal total = new BigDecimal(totalStr);

                int idNuevaFacturaGenerada = insertarFacturaEnDB(subtotal, iva, total, nomCli.getText(), idCli.getText(), dirCli.getText());

                // Insertar los detalles de venta utilizando idNuevaFacturaGenerada
                DefaultTableModel detalleModel = (DefaultTableModel) Factura.getModel();
                for (int i = 0; i < detalleModel.getRowCount(); i++) {
                    int idProducto = Integer.parseInt(detalleModel.getValueAt(i, 0).toString());
                    int cantidad = Integer.parseInt(detalleModel.getValueAt(i, 1).toString());
                    insertarDetalleVentaEnDB(idNuevaFacturaGenerada, idProducto, cantidad);
                }
                //setear el formulario en blanco
                nomCli.setText("");
                idCli.setText(" ");
                dirCli.setText("");

                DefaultTableModel model = new DefaultTableModel(
                        // Contenido de la Tabla
                        new Object[][] {
                        },
                        // Nombrar las columnas
                        new String[]{"Codigo","Cantidad","Descripción","Valor Unitario", "Valor Total"});
                // Poner el modelo hecho en el Jtable
                Factura.setModel(model);

                DefaultTableModel totalModel1 = new DefaultTableModel(
                        new Object[][] {
                                {"Subtotal", ""},
                                {"IVA (12%)", ""},
                                {"Total", ""}
                        },
                        new String[]{"", ""}); // Las columnas no necesitan nombres

                Total.setModel(totalModel1);


            }
        });

    }
    public static void setIdCajeroActual(int idCajero) {
        idCajeroActual = idCajero;
    }
    private int insertarFacturaEnDB(BigDecimal subtotal, BigDecimal iva, BigDecimal total, String nombreCliente, String cedulaCliente, String direccionCliente) {
        int numeroNotaVenta = -1;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "INSERT INTO NotaDeVenta (fecha, subtotal, iva, total, idUsuario, nombreCliente, cedulaCliente, direccionCliente) " +
                    "VALUES (CURRENT_DATE, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setBigDecimal(1, subtotal);
                statement.setBigDecimal(2, iva);
                statement.setBigDecimal(3, total);
                statement.setInt(4, idCajeroActual);
                statement.setString(5, nombreCliente);
                statement.setString(6, cedulaCliente);
                statement.setString(7, direccionCliente);
                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    numeroNotaVenta = generatedKeys.getInt(1); // Obtener el número de nota de venta generado
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return numeroNotaVenta;
    }

    private void insertarDetalleVentaEnDB(int idNotaVenta, int idProducto, int cantidad) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "INSERT INTO DetalleVenta (idTransaccion, idProducto, cantidad) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, idNotaVenta);
                statement.setInt(2, idProducto);
                statement.setInt(3, cantidad);
                statement.executeUpdate();
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idDetalleVentaGenerado = generatedKeys.getInt(1); // Obtener el idDetalleVenta generado
                    System.out.println("Número de la Transacción generada: " + idDetalleVentaGenerado);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    private void imprimirFactura() {
        Document document = new Document();
        String nombreArchivoPDF = "Nota de Venta " + numfac() + ".pdf"; // Generar el nombre del archivo con el número de factura
        try {
            PdfWriter.getInstance(document, new FileOutputStream(nombreArchivoPDF));
            document.open();

            //Imagen de cabecera
            Image header = Image.getInstance("src/images/logo.png");
            header.scaleAbsolute(100,90);
            header.setAlignment(Chunk.ALIGN_CENTER);
            document.add(header);

            //Cabecera de factura
            //Centra la cabecera
            Paragraph centro = new Paragraph();
            centro.setAlignment(Element.ALIGN_CENTER);
            document.add(new Paragraph( title.getText()));
            document.add(new Paragraph( adress.getText()));
            document.add(new Paragraph( sucursal.getText()));
            document.add(new Paragraph( ruc.getText()+"                   "+"Nota de Venta Nº " + numfac() +"\n"));
            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------\n"));
            document.add(centro);

            // Información de la factura
            //Confijuracion para la fecha en español
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd-MM-yyyy", new Locale("es", "ES"));
            String fechaHoraFormateada = ahora.format(formateador);
            document.add(new Paragraph("Fecha: " +  fechaHoraFormateada+"\n"));
            document.add(new Paragraph("Cliente: " + nomCli.getText()+"\n"));
            document.add(new Paragraph("Dirección: " + dirCli.getText()+"\n"));
            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------\n"+"\n"));


            // Detalles de los productos en la factura
            DefaultTableModel detalleModel = (DefaultTableModel) Factura.getModel();
            PdfPTable table = new PdfPTable(5); // 5 columnas para: Código, Cantidad, Descripción, Valor Unitario, Valor Total

            // Agregar encabezados de columna a la tabla PDF
            for (int i = 0; i < detalleModel.getColumnCount(); i++) {
                PdfPCell cell = new PdfPCell(new Phrase(detalleModel.getColumnName(i)));
                table.addCell(cell);
            }
            // Agregar datos de la tabla al PDF
            for (int rows = 0; rows < detalleModel.getRowCount(); rows++) {
                for (int cols = 0; cols < detalleModel.getColumnCount(); cols++) {
                    table.addCell(detalleModel.getValueAt(rows, cols).toString());
                }
            }
            // Agregar la tabla al documento PDF
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
            String QueryBuscar = "SELECT nombreProducto FROM Producto WHERE nombreProducto like '%"+codigoProducto+"%'";
            try (PreparedStatement statement = connection.prepareStatement(QueryBuscar)) {
                statement.setString(2, codigoProducto);
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
        String formatoSubtotal = formatoDecimal.format(subtotal).replace(",", "."); // Reemplaza la coma por punto
        String formatoIva = formatoDecimal.format(iva).replace(",", "."); // Reemplaza la coma por punto
        String formatoTotal = formatoDecimal.format(total).replace(",", ".");

        totalModel.setValueAt(formatoSubtotal, 0, 1); // Actualizar el valor del Subtotal en la tabla
        totalModel.setValueAt(formatoIva, 1, 1);       // Actualizar el valor del IVA en la tabla
        totalModel.setValueAt(formatoTotal, 2, 1);     // Actualizar el valor del Total en la tabla
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cajero - Menú Principal");
        frame.setContentPane(new CajeroMenu().CajeroMenu);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(2000, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    int numfac(){
        String busnumfac = "SELECT * FROM NotaDeVenta";
        try(Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(busnumfac)){
            while(rs.next()){
                fac=rs.getInt("idNotaVenta");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return fac+1;
    }

    private void mostrarproductos(){
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Id");
        modelo.addColumn("Nombre");
        modelo.addColumn("Stock");
        modelo.addColumn("Precio");

        // Poner las columnas en el modelo hecho en el Jtable
        products.setModel(modelo);

        //arreglo que almnacena datos
        String [] informacion=new String[4];//especifico el numero de columnas
        try{
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement stmt= conn.createStatement();
            ResultSet rs= stmt.executeQuery("select * from Producto");

            while (rs.next()){
                //detallo la posicion de dato almacenado en arreglo, con la columna en la quebe ir
                informacion[0]=rs.getString(1);//num de columna
                informacion[1]=rs.getString(2);
                informacion[2]=rs.getString(4);
                informacion[3]=rs.getString(5);

                // genera una fila por cada ingistro
                modelo.addRow(informacion);
            }
        } catch (SQLException e) {
            //throw new RuntimeException(e);
            JOptionPane.showMessageDialog(null,"Error"+e.toString());
        }
    }

    public void closeLoginFrame() {
        JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(CajeroMenu);
        loginFrame.dispose();}
    private void createUIComponents() {
        // TODO: place custom component creation code here
        Factura = new JTable();
        Total = new JTable();
    }
}

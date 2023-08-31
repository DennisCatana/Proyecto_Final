import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Productos {
    JPanel Productos;
    private JButton regresarButton;
    private JTextField idProducto;
    private JTextField nombreProducto;
    private JTextField descripcionProducto;
    private JTextField stock;
    private JButton buscarProductoButton;
    private JButton agregarProductoButton;
    private JButton actualizarProductoButton;
    private JButton eliminarProductoButton;
    private JTextField precio;
    private JButton limpiarButton;
    private JTable visor;
    static String DB_URL = "jdbc:mysql://localhost/medical";
    static String USER = "root";
    static String PASS = "root";
    static String QUERY = "SELECT * FROM producto";

    public Productos() {
        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame;
                frame = new JFrame("AdminMenu");
                frame.setContentPane(new AdminMenu().AdminMenu);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                closeProductosFrame();
                frame.pack();
                frame.setSize(1000, 500);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });

        buscarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProducto();
            }
        });

        agregarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(diferente(Integer.parseInt(idProducto.getText()))==false){
                    agregarProducto(Integer.parseInt(idProducto.getText()),nombreProducto.getText(),descripcionProducto.getText(),Integer.parseInt(stock.getText()),Float.parseFloat(precio.getText()));
                    JOptionPane.showMessageDialog(null,"Producto agregado");
                    limpiar();
                }
                else {
                    JOptionPane.showMessageDialog(null,"Ese id ya lo usa otro producto");
                }
            }
        });

        actualizarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarProducto(Integer.parseInt(idProducto.getText()),nombreProducto.getText(),descripcionProducto.getText(),Integer.parseInt(stock.getText()),Float.parseFloat(precio.getText()));
                JOptionPane.showMessageDialog(null,"Producto actualizado");
                limpiar();
            }
        });

        eliminarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarProducto(Integer.parseInt(idProducto.getText()));
                JOptionPane.showMessageDialog(null,"Producto eliminado");
                limpiar();
            }
        });
        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarProductos();
            }
        });
    }

    public void buscarProducto(){
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String buscar = "SELECT * FROM Producto";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(buscar);
            while (rs.next()) {
                if(Integer.parseInt(idProducto.getText()) == rs.getInt("idProducto")){
                    idProducto.setText(rs.getString("idProducto"));
                    nombreProducto.setText(rs.getString("nombreProducto"));
                    descripcionProducto.setText(rs.getString("descripcionProducto"));
                    stock.setText(String.valueOf(rs.getInt("stock")));
                    precio.setText(String.valueOf(rs.getFloat("precio")));}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void agregarProducto(int id, String nombre, String descripcion, int stock, double precio) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String agregar = "Insert into Producto (idProducto, nombreProducto, descripcionProducto, stock, precio) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(agregar);
            stmt.setInt(1, id);
            stmt.setString(2, nombre);
            stmt.setString(3, descripcion);
            stmt.setInt(4, stock);
            stmt.setDouble(5, precio);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarProducto(int id, String nuevoNombre, String nuevaDescripcion, int nuevoStock, double nuevoPrecio){
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String actualizar = "UPDATE Producto SET nombreProducto = ?, descripcionProducto = ?, stock = ?, precio = ? WHERE idProducto = ?";
            PreparedStatement stmt = conn.prepareStatement(actualizar);
            stmt.setString(1, nuevoNombre);
            stmt.setString(2, nuevaDescripcion);
            stmt.setInt(3, nuevoStock);
            stmt.setDouble(4, nuevoPrecio);
            stmt.setInt(5, id);
            stmt.executeUpdate();}
        catch (SQLException e) {
            e.printStackTrace();}}

    public void eliminarProducto(int id){
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String eliminar = "Delete From producto where idProducto  = ?";
            PreparedStatement stmt = conn.prepareStatement(eliminar);
            stmt.setInt(1, id);
            stmt.executeUpdate();}
        catch (SQLException e) {
            e.printStackTrace();}}

    public boolean diferente(int numero) {
        boolean esDiferente = false;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(QUERY)) {
            while (rs.next()) {
                int idProducto = rs.getInt("idProducto");
                if (idProducto == numero) {
                    esDiferente = true;
                    break;}}}
        catch (SQLException e) {
            throw new RuntimeException(e);}
        return esDiferente;}

    public void limpiar(){
        JFrame frame = new JFrame("Productos");
        frame.setContentPane(new Productos().Productos);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        closeProductosFrame();
        frame.setSize(1000, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void mostrarProductos(){
        //genera columnas de la tabla
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("IdProducto");
        modelo.addColumn("Nombre");
        modelo.addColumn("Descripción");
        modelo.addColumn("Stock");
        modelo.addColumn("Precio");

        // Poner las columnas en el modelo hecho en el Jtable
        visor.setModel(modelo);

        //arreglo que almnacena datos
        String [] informacion=new String[5];//especifico el numero de columnas
        try{
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement stmt= conn.createStatement();
            ResultSet rs= stmt.executeQuery(QUERY);

            while (rs.next()){
                //detallo la posicion de dato almacenado en arreglo, con la columna en la quebe ir
                informacion[0]=rs.getString(1);//num de columna
                informacion[1]=rs.getString(2);
                informacion[2]=rs.getString(3);
                informacion[3]=rs.getString(4);
                informacion[4]=rs.getString(5);

                // genera una fila por cada ingistro
                modelo.addRow(informacion);
            }
        } catch (SQLException e) {
            //throw new RuntimeException(e);
            JOptionPane.showMessageDialog(null,"Error"+e.toString());
        }
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

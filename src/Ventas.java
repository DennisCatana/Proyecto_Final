import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Ventas {
     JPanel Ventas;
    private JButton regresarButton;
    private JTable ventas;
    private JButton buscarButton;
    private JTextField buscarCajero;
    private JButton ventasButton;
    static final String DB_URL="jdbc:mysql://localhost/Medical";
    static final String USER="root";
    static final String PASS="root";
    static final String QUERY="Select * From NotaDeVenta";

    public Ventas() {
        //genera columnas de la tabla
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("idUsuario");
        model.addColumn("idNotaVenta");
        model.addColumn("fecha");
        model.addColumn("subtotal");
        model.addColumn("iva");
        model.addColumn("Total");
        model.addColumn("nombreCliente");
        model.addColumn("cedulaCliente");
        model.addColumn("direccionCliente");

        // Poner las columnas en el modelo hecho en el Jtable
        ventas.setModel(model);

        //arreglo que almnacena registro de ventas
        String [] infoVentas=new String[9];//especifico el numero de columnas

        try{
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement stmt= conn.createStatement();
            ResultSet rs= stmt.executeQuery(QUERY);

            while (rs.next()){
                //detallo la posicion de dato almacenado en arreglo, con la columna en la quebe ir
                infoVentas[0]=rs.getString(6);//num de columna
                infoVentas[1]=rs.getString(1);
                infoVentas[2]=rs.getString(2);
                infoVentas[3]=rs.getString(4);
                infoVentas[4]=rs.getString(5);
                infoVentas[5]=rs.getString(3);
                infoVentas[6]=rs.getString(7);
                infoVentas[7]=rs.getString(8);
                infoVentas[8]=rs.getString(9);

                // genera una fila por cada ingistro
                model.addRow(infoVentas);
            }
        } catch (SQLException e) {
            //throw new RuntimeException(e);
            JOptionPane.showMessageDialog(null,"Error"+e.toString());
        }

        regresarButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame;
            frame = new JFrame("AdminMenu");
            frame.setContentPane(new AdminMenu().AdminMenu);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            closeVentasFrame();
            frame.pack();
            frame.setSize(1000, 500);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    });
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarIdUsuario(Integer.parseInt(buscarCajero.getText()));
            }
        });
        ventasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("idUsuario");
                model.addColumn("idNotaVenta");
                model.addColumn("fecha");
                model.addColumn("subtotal");
                model.addColumn("iva");
                model.addColumn("Total");
                model.addColumn("nombreCliente");
                model.addColumn("cedulaCliente");
                model.addColumn("direccionCliente");

                // Poner las columnas en el modelo hecho en el Jtable
                ventas.setModel(model);

                //arreglo que almnacena registro de ventas
                String [] infoVentas=new String[9];//especifico el numero de columnas

                try{
                    Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
                    Statement stmt= conn.createStatement();
                    ResultSet rs= stmt.executeQuery(QUERY);

                    while (rs.next()){
                        //detallo la posicion de dato almacenado en arreglo, con la columna en la quebe ir
                        infoVentas[0]=rs.getString(6);//num de columna
                        infoVentas[1]=rs.getString(1);
                        infoVentas[2]=rs.getString(2);
                        infoVentas[3]=rs.getString(4);
                        infoVentas[4]=rs.getString(5);
                        infoVentas[5]=rs.getString(3);
                        infoVentas[6]=rs.getString(7);
                        infoVentas[7]=rs.getString(8);
                        infoVentas[8]=rs.getString(9);

                        // genera una fila por cada ingistro
                        model.addRow(infoVentas);
                    }
                } catch (SQLException m) {
                    //throw new RuntimeException(e);
                    JOptionPane.showMessageDialog(null,"Error"+m.toString());
                }
            }
        });
    }

    public void buscarIdUsuario(int id){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("idUsuario");
        model.addColumn("idNotaVenta");
        model.addColumn("fecha");
        model.addColumn("subtotal");
        model.addColumn("iva");
        model.addColumn("Total");
        model.addColumn("nombreCliente");
        model.addColumn("cedulaCliente");
        model.addColumn("direccionCliente");
        ventas.setModel(model);
        String [] infoVentas=new String[9];//especifico el numero de columnas
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM NotaDeVenta WHERE idUsuario = ?")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                if(rs.getInt("idUsuario")==id){
                    infoVentas[0]=rs.getString(6);//num de columna
                    infoVentas[1]=rs.getString(1);
                    infoVentas[2]=rs.getString(2);
                    infoVentas[3]=rs.getString(4);
                    infoVentas[4]=rs.getString(5);
                    infoVentas[5]=rs.getString(3);
                    infoVentas[6]=rs.getString(7);
                    infoVentas[7]=rs.getString(8);
                    infoVentas[8]=rs.getString(9);
                    model.addRow(infoVentas);
                }
                else {
                    JOptionPane.showMessageDialog(null, "No se encontró un registro con ese ID");

            }
                buscarCajero.setText("");

            }
            }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeVentasFrame() {
        JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(Ventas);
        loginFrame.dispose();
    }
}

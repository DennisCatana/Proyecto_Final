import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Ventas {
     JPanel Ventas;
    private JButton regresarButton;
    private JTable ventas;
    static final String DB_URL="jdbc:mysql://localhost/Medical";
    static final String USER="root";
    static final String PASS="root";
    static final String QUERY="Select * From NotaDeVenta";

    public Ventas() {
        //genera columnas de la tabla
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("idNotaVenta");
        model.addColumn("fecha");
        model.addColumn("Total");
        model.addColumn("subtotal");
        model.addColumn("iva");
        model.addColumn("idUsuario");
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
                infoVentas[0]=rs.getString(1);//num de columna
                infoVentas[1]=rs.getString(2);
                infoVentas[2]=rs.getString(3);
                infoVentas[3]=rs.getString(4);
                infoVentas[4]=rs.getString(5);
                infoVentas[5]=rs.getString(6);
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
}

    private void closeVentasFrame() {
        JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(Ventas);
        loginFrame.dispose();
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Ventas");
        frame.setContentPane(new Ventas().Ventas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

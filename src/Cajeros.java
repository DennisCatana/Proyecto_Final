import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Cajeros {
    JPanel Cajeros;
    private JButton regresarButton;
    private JTable visor;
    private JButton mostarCajerosButton;
    private JTextField nombre;
    private JTextField apellido;
    private JTextField rol;
    private JTextField salario;
    private JTextField id;
    private JTextField contra;
    private JTextField fIngreso;
    private JButton buscarButton;
    private JButton ingresarInformaciónButton;
    private JButton eliminarUsuarioButton;
    private JButton actualizarInformaciónButton;

    static final String DB_URL="jdbc:mysql://localhost/Medical";
    static final String USER="root";
    static final String PASS="root";

    //la tabla usuarios contiene cajeros y administradores, por ello especifico que tipo de usuario deseo visualizar en la tabla
    static final String QUERY="Select * From Usuario WHERE tipoUsuario = 'cajero'";


    //variables de ingreso que sirven para comprobar los regsitros
    String nomx;
    String apex;
    String rolx;
    String suelx;
    String idx ;
    String contrax;
    String fingrx;
    public Cajeros(){
    regresarButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame;
            frame = new JFrame("AdminMenu");
            frame.setContentPane(new AdminMenu().AdminMenu);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            closeCajerosFrame();
            frame.pack();
            frame.setSize(1000, 500);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    });
        mostarCajerosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Mostrar();
            }
        });
        ingresarInformaciónButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //relacion de los ingresos con cada JTextField
                idx = id.getText();
                nomx = nombre.getText().trim();
                apex = apellido.getText().trim();
                rolx = rol.getText().trim();
                contrax = contra.getText().trim();
                suelx = salario.getText().trim();
                fingrx = fIngreso.getText().trim();

                //paso parametros al metodo
                Ingresar(idx, nomx, apex, rolx, contrax, suelx, fingrx);
            }
        });
        eliminarUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idx = id.getText();
                Eliminar(idx);
            }
        });
        actualizarInformaciónButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idx = id.getText();
                nomx = nombre.getText().trim();
                apex = apellido.getText().trim();
                rolx = rol.getText().trim();
                contrax = contra.getText().trim();
                suelx = salario.getText().trim();
                fingrx = fIngreso.getText().trim();

                Actualizar(idx, nomx, apex, rolx, contrax, suelx, fingrx);
            }
        });
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idx = id.getText();
                Buscar(idx);
            }
        });
    }


    //cerrar la jframe cuando me direcciono hacia otra
    private void closeCajerosFrame(){
        JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(Cajeros);
        loginFrame.dispose();
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Cajeros");
        frame.setContentPane(new Cajeros().Cajeros);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    //Metodos de los botones
    public void Mostrar(){
        //genera columnas de la tabla
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("idUsuario");
        model.addColumn("nombre");
        model.addColumn("Apellido");
        model.addColumn("Rol");//columna tipoUsuario
        model.addColumn("Contraseña");
        model.addColumn("Salario");
        model.addColumn("FechaIngreso");

        // Poner las columnas en el modelo hecho en el Jtable
        visor.setModel(model);

        //arreglo que almnacena datos
        String [] informacion=new String[7];//especifico el numero de columnas

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
                informacion[5]=rs.getString(6);
                informacion[6]=rs.getString(7);

                // genera una fila por cada ingistro
                model.addRow(informacion);
            }
        } catch (SQLException e) {
            //throw new RuntimeException(e);
            JOptionPane.showMessageDialog(null,"Error"+e.toString());
        }
    }

    public void Ingresar(String idU, String nom, String ape, String trol, String cont, String suel, String ingre){
        //es necesario definir TODOS los registros que se encuentran designados en la tabla
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Usuario (idUsuario, nombre, apellido, tipoUsuario, contraseña, salario, fechaContratacion) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            //parametros que se contemplan en la tabla generada en mysql

                //Conversiones de tipo String hacia el tipo de tado establecido en la tabla
            pstmt.setInt(1, Integer.parseInt(idU));
            pstmt.setString(2, nom);
            pstmt.setString(3, ape);
            pstmt.setString(4, trol);
            pstmt.setString(5, cont);
            pstmt.setDouble(6, Double.parseDouble(suel));
            pstmt.setString(7, ingre);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Informacion ingresada correctamente");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Eliminar( String idU){
        //busco el usuario en funcon de su id(primary key), para eliminar toda la informacion de ese id
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             //genero la busqueda considerando solo el ID
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Usuario WHERE idUsuario = ?")) {
            pstmt.setInt(1, Integer.parseInt(idU));
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                //si el registro con ese id se encuenta en la lista
                JOptionPane.showMessageDialog(null, "Registro eliminado correctamente");
            } else {
                //si el registro con ese id  NO se encuenta en la lista
                JOptionPane.showMessageDialog(null, "No se encontró un registro con ese ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void Actualizar(String idU, String nom, String ape, String trol, String cont, String suel, String ingre){

        //genenero una busqueda interna por id para seleccionar el usuario y asi editar la informacion con ese id
            //se puede acualizar la infomacion de cualquier campo menos el Id
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE Usuario SET nombre = ?, apellido = ?, tipoUsuario = ?, contraseña = ?, salario = ?, fechaContratacion = ? WHERE idUsuario = ?")) {

            pstmt.setString(1, nom);
            pstmt.setString(2, ape);
            pstmt.setString(3, trol);
            pstmt.setString(4, cont);
            pstmt.setDouble(5, Double.parseDouble(suel));
            pstmt.setString(6, ingre);
            pstmt.setInt(7, Integer.parseInt(idU));

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Registro actualizado correctamente");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un registro con ese ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void Buscar(String idU) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("idUsuario");
        model.addColumn("nombre");
        model.addColumn("Apellido");
        model.addColumn("Rol");
        model.addColumn("Contraseña");
        model.addColumn("Salario");
        model.addColumn("FechaIngreso");
        visor.setModel(model);

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Usuario WHERE idUsuario = ?")) {
            pstmt.setInt(1, Integer.parseInt(idU));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String[] informacion = new String[7];
                informacion[0] = rs.getString(1);
                informacion[1] = rs.getString(2);
                informacion[2] = rs.getString(3);
                informacion[3] = rs.getString(4);
                informacion[4] = rs.getString(5);
                informacion[5] = rs.getString(6);
                informacion[6] = rs.getString(7);
                model.addRow(informacion);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un registro con ese ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

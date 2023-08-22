import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {

    JPanel Login;
    private JComboBox<String> rolBox;
    private JTextField user;
    private JPasswordField password;
    private JPanel Imagen;
    private JPanel Datos;
    private JButton entrarButton;
    private String seleccionar;
    String admin="admin";
    String contradmin="admin";
    String cajero="caje";
    String contracajero="caje";



    public Login() {
        // Para que al momento de iniciar el Form no aparezca señalado ningun botón
        Login.setFocusable(true);
        Login.requestFocusInWindow();

        // Lógica para el inicio de sesión
        entrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Se usa una variable para que almacene la informacion del ComboBox
                seleccionar = (String) rolBox.getSelectedItem();
                JFrame frame;
                //Se usa una variable boolean que arroja true si hay informacion en el textfile
                boolean hayInformacionUsuario = !user.getText().isEmpty();

                //Se usa una variable boolean que arroja true si hay informacion en el passwordfield
                boolean hayInformacionContrasenia = !password.getText().isEmpty();
                //Condicional que verifica si hay informacion en el textfile o en el passwordfield
                if(hayInformacionUsuario==true || hayInformacionContrasenia==true){

                    //Condicional que verifica que se ingrese el usuario y la contraseña del administrador o del cajero
                    if (admin.equals(user.getText()) && contradmin.equals(new String(password.getPassword())) || cajero.equals(user.getText()) && contracajero.equals(new String(password.getPassword()))) {

                        //Condicional que verifica que el comboBox este en Administrador y que se ingresen el usuario y contraseña del administrador
                        if (seleccionar.equals("Administrador") && admin.equals(user.getText()) && contradmin.equals(new String(password.getPassword()))) {
                            frame = new JFrame("Administrador");
                            frame.setContentPane(new AdminMenu().AdminMenu);
                            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            closeLoginFrame();
                            frame.pack();
                            frame.setSize(1000, 500);
                            frame.setLocationRelativeTo(null);
                            frame.setVisible(true);}

                        //Condicional que verifica que el comboBox este en Cajero y que se ingresen el usuario y contraseña del cajero
                        else if (seleccionar.equals("Cajero") && cajero.equals(user.getText()) && contracajero.equals(new String(password.getPassword()))) {
                            frame = new JFrame("Cajero");
                            frame.setContentPane(new Cajeros().Cajeros);
                            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            closeLoginFrame();
                            frame.pack();
                            frame.setSize(1000, 500);
                            frame.setLocationRelativeTo(null);
                            frame.setVisible(true);}}

                    //Condicional que indica si no se ha ingresado el usuario y la contraseña del administrador o del cajero
                    else {
                        frame = new JFrame("Credenciales");
                        frame.setContentPane(new Credenciales().crede);
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.pack();
                        frame.setSize(700, 350);
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);}}

                //Condicional que indica que no se ha colocado las credenciales correctas
                else{
                    frame = new JFrame("Incorrecto");
                    frame.setContentPane(new Incorrecto().inco);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.pack();
                    frame.setSize(700, 350);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    hayInformacionUsuario=true;
                    hayInformacionContrasenia=true;}

                //Condicional que indica que no se ha seleccionado ninguna opcion del comboBox
                if (seleccionar.equals("Seleccione") && hayInformacionUsuario==false && hayInformacionContrasenia==false){
                    frame = new JFrame("Incorrecto");
                    frame.setContentPane(new Incorrecto().inco);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.pack();
                    frame.setSize(700, 350);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);}
            }
        });
    }

    //Funcion que cierra el form anterior y deja que siga funcionando el programa
    private void closeLoginFrame() {
        JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(Login);
        loginFrame.dispose();}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setContentPane(new Login().Login);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 350);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}


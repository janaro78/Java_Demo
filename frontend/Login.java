package frontend;

import backend.Admin;
import backend.SuperPlantilla;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;

public class Login extends SuperPlantilla {

    public static final String URL = "jdbc:mysql://PMYSQL171.dns-servicio.com:3306/10029755_21GIIN";
    public static final String sqlUSER = "21giin";
    public static final String sqlPASSWORD = "AnFYE3q4FQxs6ka";

    private JTextField user;
    private JPasswordField pass;
    private JButton loginButton;
    private int idCliente;

    // Constructor
    public Login() {
        super("Login");
        setLayout(new GridLayout(0, 1)); // Use GridLayout with a single column

        user = new JTextField(60);
        user.setHorizontalAlignment(JTextField.CENTER);
        pass = new JPasswordField(48);
        pass.setHorizontalAlignment(JTextField.CENTER);
        loginButton = new JButton("Acceder");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Añadir estos elementos al GUI
        add(new JLabel("Nombre:"));
        add(user);
        add(new JLabel("Contraseña:"));
        add(pass);
        add(loginButton);
    }

    // Ocultar ventana
    private void hideLoginWindow() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(loginButton.getParent());
        frame.setVisible(false);
    }

    public int getIdCliente() {
        return idCliente;
    }

    private void showAdminView(boolean isAdmin) {
        if (isAdmin) {
            Admin adminView = new Admin(); // Create an instance of Admin window
            adminView.setVisible(true);
        } else {
            MiAdmin miAdminView = new MiAdmin(idCliente); // Crear nueva instancia de MiAdmin
            miAdminView.setVisible(true);
        }
    }

    // Validar usuarios
    private void performLogin() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, sqlUSER, sqlPASSWORD);
            Statement stmt = con.createStatement();

            char[] passwordChars = pass.getPassword();
            String password = new String(passwordChars);

            String sql = "SELECT * FROM `10029755_21GIIN`.`usuarios` WHERE Nombre = '" + user.getText() + "'";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String retrievedPassword = rs.getString("Contraseña").trim();
                String userType = rs.getString("TipodeUsuario").trim();

                if (retrievedPassword.equals(password)) {
                    JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(loginButton.getParent());
                    JOptionPane.showMessageDialog(null, "Inicio realizado con éxito.");
                    hideLoginWindow();

                    // Store the idCliente when the login is successful
                    idCliente = rs.getInt("idCliente");

                    // Determine user role based on the 'TipodeUsuario' column
                    boolean isAdmin = userType.equalsIgnoreCase("Administrador");

                    // Open different views based on user type
                    if (isAdmin) {
                        Admin adminView = new Admin(); // Create an instance of Admin window
                        adminView.setVisible(true);
                    } else {
                        // Pass the idCliente to MiAdmin constructor
                        MiAdmin miAdminView = new MiAdmin(idCliente);
                        miAdminView.setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Contraseña incorrecta.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado en el sistema.");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error de login: " + e.getMessage());
        }
    }


    // Punto de entrada al programa
    public static void main(String[] args) throws ParseException {
        SwingUtilities.invokeLater(() -> {
            Login loginPanel = new Login();
            loginPanel.setVisible(true);
        });
    }
}
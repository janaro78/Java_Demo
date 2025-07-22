package backend;
// 23:42

import com.toedter.calendar.JDateChooser;
import data.JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddUsuarios extends SuperPlantilla {

    // JButton showAdminView en Superclase
    private JTextField textFieldNombre;
    private JTextField textFieldApellido1;
    private JTextField textFieldApellido2;
    private JTextField textFieldDireCorreo;
    private JTextField textFieldContraseña;
    private JDateChooser dateFieldFechaAlta;
    private JDateChooser dateFieldFechaBaja;
    private JTextField textFieldZonaHoraria;
    private JTextField textFieldNombreEmpresa;
    private JTextField textFieldidFiscal;
    private JComboBox<String> TiposdeUsuario;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public AddUsuarios() {
        super("Añadir usuario"); // Título de la ventana
        // Configuración básica de la ventana
        setLayout(new GridLayout(0, 1, 10, 10));   // Configuración del diseño del panel

        // Campos de entrada
        textFieldNombre = new JTextField(20);
        textFieldApellido1 = new JTextField(20);
        textFieldApellido2 = new JTextField(20);
        textFieldDireCorreo = new JTextField(50);
        textFieldContraseña = new JTextField(50);
        dateFieldFechaAlta = new JDateChooser();
        dateFieldFechaBaja = new JDateChooser();
        textFieldZonaHoraria = new JTextField((20));
        textFieldNombreEmpresa = new JTextField((50));
        textFieldidFiscal = new JTextField(20);
        String[] options = {"Administrador", "JefeZona", "JefeDepartamento", "Operario", "Contable", "Cliente"};
        TiposdeUsuario = new JComboBox<>(options);

        // Crear un panel con GridLayout
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        // Definimos nuevo alto del software
        setSize(new Dimension(800, 1200));
        // Centrar ventana
        setLocationRelativeTo(null);

        // Agregar los campos de texto y el botón al panel
        add(showAdminView);
        add(new JLabel(("Nombre:")));
        add(textFieldNombre);
        add(new JLabel(("Primer apellido:")));
        add(textFieldApellido1);
        add(new JLabel(("Segundo apellido:")));
        add(textFieldApellido2);
        add(new JLabel(("Dirección de correo electrónico:")));
        add(textFieldDireCorreo);
        add(new JLabel("Contraseña"));
        add(textFieldContraseña);
        add(new JLabel("Fecha de alta (YYYY-MM-DD HH:mm:ss):"));
        add(dateFieldFechaAlta);
        add(new JLabel("Fecha de baja (YYYY-MM-DD HH:mm:ss):"));
        add(dateFieldFechaBaja);
        add(new JLabel(("Zona horaria:")));
        add(textFieldZonaHoraria);
        add(new JLabel("Nombre de la empresa"));
        add(textFieldNombreEmpresa);
        add(new JLabel("Número fiscal:"));
        add(textFieldidFiscal);
        add(new JLabel(("Seleccionar tipo de usuario:")));
        add(TiposdeUsuario);

        // Crear un botón que guarda en la base de datos al hacer clic
        JButton buttonGuardar = new JButton("Añadir Usuario");
        add(buttonGuardar);
        buttonGuardar.addActionListener(e ->
                guardarEnBD());             // Llama método para guardar usuario en BD

        // Agregar el panel a la ventana
        add(panel);

        // Hacer visible la ventana
        setVisible(true);

        // Acción/botón para volver a la consola de administración (inicio)
        showAdminView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAdminView();
                setVisible(false); // Oculta ventana Usuarios
                showAdminView.setVisible(true); // Muestra ventana AddUsuarios
            }
        });
    }


    // Método para guardar un usuario en la base de datos
    public void guardarEnBD() {
        // Obtener entradas de los campos de texto
        String nombre = textFieldNombre.getText();
        String apellido1 = textFieldApellido1.getText();
        String apellido2 = textFieldApellido2.getText();
        String dirCorreo = textFieldDireCorreo.getText();
        String contraseña = textFieldContraseña.getText();

        Date fechaAltaDate = dateFieldFechaAlta.getDate();
        Date fechaBajaDate = dateFieldFechaBaja.getDate();
        String formattedFechaAlta = (fechaAltaDate != null) ? dateFormat.format(fechaAltaDate) : null;
        String formattedFechaBaja = (fechaBajaDate != null) ? dateFormat.format(fechaBajaDate) : null;

        String ZonaHoraria = textFieldZonaHoraria.getText();
        String NombreEmpresa = textFieldNombreEmpresa.getText();
        String idFiscal = textFieldidFiscal.getText();
        String selectUser = (String) TiposdeUsuario.getSelectedItem();


        // Llamar al método de la clase JDBC para insertar el usuario en la base de datos
        JDBC.insertarUsuario(nombre, apellido1, apellido2, dirCorreo, contraseña, formattedFechaAlta, formattedFechaBaja, ZonaHoraria, NombreEmpresa, idFiscal, selectUser);

        // Mostrar un JOptionPane de mensaje con los datos ingresados
        JOptionPane.showMessageDialog(this, "Usuario añadido correctamente.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
    }
}

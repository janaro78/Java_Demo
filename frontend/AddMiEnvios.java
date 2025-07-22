package frontend;

import backend.SuperPlantilla;
import data.JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class AddMiEnvios extends SuperPlantilla {

    // JButton showAdminView en Superclase
    private JTextField intNumPaquete;
    private JTextField inputfechaSalida;
    private JTextField inputfechaLlegada;
    private JTextField floatVolumenTotal;
    private JTextField floatEmpaquetado;
    private JTextField textTarjetaCredito;
    private JTextField pickupTipoVia;   // Combo dropdown
    private JTextField deliveryTipoVia; // Combo dropdown
    private JTextField cliente;         // Combo dropdown
    private JTextField CosteAduanero;
    private JTextField Ruta;            // Combo dropdown
    private JTextField TipodeTranporte; // Combo dropdown
    private JComboBox<String> pickupTipoViaComboBox;    // * Combobox inicializado para Origen
    private JComboBox<String> deliveryTipoViaComboBox;  // * Combobox inicializado para Destino
    private JComboBox<String> clienteCombobox;          // * Combobox inicializado para Cliente
    private JComboBox<String> nomEstadoCombo;              // * Combobox inicializado para Estado
    private int currentCliente;


    public AddMiEnvios(int idCliente){
        super("Crear mi envío");
        // Configuración básica de la ventana
        setLayout(new GridLayout(0, 1, 10, 10));   // Configuración del diseño del panel

        // Campos de entrada
        intNumPaquete = new JTextField(50);
        inputfechaSalida = new JTextField(50);
        inputfechaLlegada = new JTextField(30);
        floatVolumenTotal = new JTextField(40);
        floatEmpaquetado = new JTextField(40);
        textTarjetaCredito = new JTextField(50);
        pickupTipoVia = new JTextField(50);
        deliveryTipoVia = new JTextField(50);
        cliente = new JTextField(50);
        currentCliente = idCliente;

        // Mostrar los siguientes campos en el panel swing:
        add(showAdminView);

        // Entradas para añadir nuevo envío
        add(new JLabel(("Paquete:")));
        add(intNumPaquete);
        add(new JLabel(("Fecha de salida: (yyyy-MM-dd)")));
        add(inputfechaSalida);
        add(new JLabel("Fecha de llegada: (yyyy-MM-dd)"));
        add(inputfechaLlegada);
        add(new JLabel(("Volumen Total:")));
        add(floatVolumenTotal);
        add(new JLabel(("Precio de empaquetado:")));
        add(floatEmpaquetado);
        add(new JLabel(("Nº de tarjeta de crédito:")));
        add(textTarjetaCredito);

        // Origen
        add(new JLabel(("Domicilio origen:")));
        pickupTipoViaComboBox = new JComboBox<>();
        add(pickupTipoViaComboBox);
        List<String> tipoViaValues = JDBC.getUniqueTipoViaValues();     // * Lista de Strings con valores únicos de la columna TipoVia
        String[] arrayTipoVia = tipoViaValues.toArray(new String[0]);  // * Convertir la lista a un array de Strings (JComboBox solo acepta arrays)
        DefaultComboBoxModel<String> inputTipoVia = new DefaultComboBoxModel<>(arrayTipoVia); // * Crear un modelo de ComboBox con el array
        pickupTipoViaComboBox.setModel(inputTipoVia); // * Asignar inputTipoVia al JComboBox

        // Destino
        add(new JLabel(("Domicilio destino:")));
        deliveryTipoViaComboBox = new JComboBox<>();
        add(deliveryTipoViaComboBox);
        List<String> tipoViaValuesDestino = JDBC.getUniqueTipoViaValues();     // * Lista de Strings con valores únicos de la columna TipoVia
        String[] arrayTipoViaDestino = tipoViaValuesDestino.toArray(new String[0]);  // * Convertir la lista a un array de Strings (JComboBox solo acepta arrays)
        DefaultComboBoxModel<String> inputTipoViaDestino = new DefaultComboBoxModel<>(arrayTipoViaDestino); // * Crear un modelo de ComboBox con el array
        deliveryTipoViaComboBox.setModel(inputTipoViaDestino); // * Asignar inputTipoVia al JComboBox

        // JCombo para seleccionar Cliente
        int currentCliente = idCliente;

        // Crear un botón que guarda en la base de datos al hacer clic
        JButton addShipping = new JButton("Añadir envío");
        add(addShipping);
        addShipping.addActionListener(e ->
                addMiShippingToBD());             // Llama método para guardar dirección en BD

        // Crear un panel con GridLayout
        JPanel AddressPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        // Agregar el panel a la ventana
        add(AddressPanel);
        // Hacer visible la ventana
        setVisible(true);

        // Nos lleva a panel de administración/inicio
        showAdminView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MiAdmin showMiAdminView =new MiAdmin(idCliente);
                setVisible(false); // Oculta ventana Usuarios
                showMiAdminView.setVisible(true); // Muestra ventana AddUsuarios
            }
        });

        // Modificar el la altura de la ventana
        setSize(new Dimension(800, 1000));

        // Centrar ventana
        setLocationRelativeTo(null);
    }

    // Método para guardar un usuario en la base de datos
    public void addMiShippingToBD() {
        // Obtener entradas de los campos de texto
        Integer NumPaquete = Integer.valueOf(intNumPaquete.getText());
        String fechaSalida = inputfechaSalida.getText();
        String fechaLlegada = inputfechaLlegada.getText();
        Float VolumenTotal = Float.valueOf(floatVolumenTotal.getText());
        String empaquetado = floatEmpaquetado.getText();
        String TarjetaCredito = textTarjetaCredito.getText();
        String pickupTipoViaValue = (String) pickupTipoViaComboBox.getSelectedItem();
        String deliveryTipoViaValue = (String) deliveryTipoViaComboBox.getSelectedItem();
        int Cliente = currentCliente;

        // Llamar al método de la clase JDBC para insertar la dirección en la base de datos
        JDBC.insertMiShipping(NumPaquete, fechaSalida, fechaLlegada, VolumenTotal, empaquetado, TarjetaCredito, pickupTipoViaValue, deliveryTipoViaValue, Cliente);

        // Mostrar un JOptionPane de mensaje con los datos ingresados
        JOptionPane.showMessageDialog(this, "Envío añadido correctamente.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
    }
}

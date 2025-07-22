package frontend;

import backend.SuperPlantilla;
import data.JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddMiAddress extends SuperPlantilla {

    // JButton showAdminView en Superclase
    private JTextField textTipoVia;
    private JTextField intNumDomicilio;
    private JTextField textCodigoPostal;
    private JTextField textProvincia;
    private JTextField textPais;
    private JTextField textTipoDomicilio;
    private JTextField floatTarifaAduanas;
    private JTextField floatCantidadImpuestosAduanero;
    private JTextField textLocalidad;
    private int idCliente;

    public AddMiAddress(int idCliente){
        super("Añadir dirección");
        // Configuración básica de la ventana
        setLayout(new GridLayout(0, 1, 10, 10));   // Configuración del diseño del panel
        this.idCliente = idCliente; // Asignar idCliente

        //Campos de entrada
        textTipoVia = new JTextField(50);
        intNumDomicilio = new JTextField(30);
        textCodigoPostal = new JTextField(30);
        textProvincia = new JTextField(40);
        textPais = new JTextField(40);
        textTipoDomicilio = new JTextField(30);
        floatTarifaAduanas = new JTextField(40);
        floatCantidadImpuestosAduanero = new JTextField(40);
        textLocalidad = new JTextField(40);

    // Mostrar los siguientes campos en el panel swing:
        add(showAdminView);

        add(new JLabel(("Tipo de vía:")));
        add(textTipoVia);
        add(new JLabel(("Número de domicilio:")));
        add(intNumDomicilio);
        add(new JLabel("Localidad:"));
        add(textLocalidad);
        add(new JLabel(("Código postal:")));
        add(textCodigoPostal);
        add(new JLabel(("Provincia:")));
        add(textProvincia);
        add(new JLabel(("País:")));
        add(textPais);
        add(new JLabel("Tipo de domicilio:"));
        add(textTipoDomicilio);
        add(new JLabel("Tarifa aduanera:"));
        add(floatTarifaAduanas);
        add(new JLabel(("Cantidad de impuestos aduaneros:")));
        add(floatCantidadImpuestosAduanero);

        // Crear un botón que guarda en la base de datos al hacer clic
        JButton saveAddress = new JButton("Añadir dirrección");
        add(saveAddress);
        saveAddress.addActionListener(e ->
            saveAddressToBD());             // Llama método para guardar dirección en BD

        // Crear un panel con GridLayout
        JPanel AddressPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        // Agregar el panel a la ventana
        add(AddressPanel);
        // Hacer visible la ventana
        setVisible(true);

        showAdminView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MiAdmin miAdminView = new MiAdmin(idCliente); // Crear nueva instancia de MiAdmin
                setVisible(false); // Oculta ventana Admin
                miAdminView.setVisible(true);
            }
        });
    }

    // Método para guardar un usuario en la base de datos
    public void saveAddressToBD() {
        // Obtener entradas de los campos de texto
        String TipoVia = textTipoVia.getText();
        Integer NumDomicilio = Integer.valueOf(intNumDomicilio.getText());
        String CodigoPostal = textCodigoPostal.getText();
        String Provincia  = textProvincia.getText();
        String País = textPais.getText();
        String TipoDomilicio = textTipoDomicilio.getText();
        Float TarifaAduanas = Float.valueOf(floatTarifaAduanas.getText());
        Float CantidadImpuestosAduanero = Float.valueOf(floatCantidadImpuestosAduanero.getText());
        String Localidad = textLocalidad.getText();


        // Llamar al método de la clase JDBC para insertar la dirección en la base de datos
        JDBC.insertAddress(TipoVia, NumDomicilio, CodigoPostal, Provincia, País, TipoDomilicio, TarifaAduanas, CantidadImpuestosAduanero, Localidad);

        // Mostrar un JOptionPane de mensaje con los datos ingresados
        JOptionPane.showMessageDialog(this, "Usuario añadido correctamente.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
    }
}

package backend;

import data.JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddRuta extends SuperPlantilla {

    // JButton showAdminView en Superclase
    private final JTextField textCosteRuta;
    private final JTextField textCosteDistancia;
    private final JTextField textDescripcionRuta;

    public AddRuta() {
        super("Añadir Ruta");
        // Configuración básica de la ventana
        setLayout(new GridLayout(0, 1, 10, 10));   // Configuración del diseño del panel

        //Campos de entrada
        textCosteRuta = new JTextField(50);
        textCosteDistancia = new JTextField(30);
        textDescripcionRuta = new JTextField(40);

        // Mostrar los siguientes campos en el panel swing:
        add(showAdminView);

        add(new JLabel(("Coste de Ruta:")));
        add(textCosteRuta);
        add(new JLabel(("Coste de Distancia:")));
        add(textCosteDistancia);
        add(new JLabel("Descripción de Ruta:"));
        add(textDescripcionRuta);

        // Crear un botón que guarda en la base de datos al hacer clic
        JButton saveRuta = new JButton("Añadir Ruta");
        add(saveRuta);
        saveRuta.addActionListener(e ->
                saveRutaToBD());             // Llama método para guardar ruta en BD

        // Crear un panel con GridLayout
        JPanel rutaPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        // Agregar el panel a la ventana
        add(rutaPanel);
        // Hacer visible la ventana
        setVisible(true);

        showAdminView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAdminView();
                setVisible(false); // Oculta ventana Rutas
                showAdminView.setVisible(true); // Muestra ventana AddUsuarios
            }
        });
    }

    // Método para guardar una ruta en la base de datos
    public void saveRutaToBD() {
        // Obtener entradas de los campos de texto
        Float costeRuta = Float.valueOf(textCosteRuta.getText());
        Float costeDistancia = Float.valueOf(textCosteDistancia.getText());
        String descripcionRuta = textDescripcionRuta.getText();

        // Llamar al método de la clase JDBC para insertar la ruta en la base de datos
        JDBC.insertRuta(costeRuta, costeDistancia, descripcionRuta);

        // Mostrar un JOptionPane de mensaje con los datos ingresados
        JOptionPane.showMessageDialog(this, "Ruta añadida correctamente.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
    }
}

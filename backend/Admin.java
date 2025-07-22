package backend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Admin extends SuperPlantilla {
    // Constructor
    public Admin() {
        super("Admin");
        setLayout(new GridLayout(0, 1)); // Use GridLayout with a single column

        // Campos de entrada
        JButton invoicingButton = new JButton("Facturación");
        JButton shippingButton = new JButton("Envíos");
        JButton usersButton = new JButton("Usuarios");
        JButton addressesButton = new JButton("Direcciones");
        JButton rutasButton = new JButton("Rutas");
        JButton MediosTransButton = new JButton("Medios de Transporte");

        // Abre GUI para medios de transporte
        MediosTransButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MediosTransporte mediosTransporte = new MediosTransporte();
                setVisible(false); // Oculta ventana Admin
                mediosTransporte.setVisible(true);
            }
        });

        // Abre GUI para rutas
        rutasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Rutas rutas = new Rutas();
                setVisible(false);
                rutas.setVisible(true);
            }
        });

        // Abre GUI para usuarios
        usersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Usuarios usuarios = new Usuarios();
                setVisible(false); // Hide the Admin frame
                usuarios.setVisible(true);
            }
        });

        // Abre GUI para direcciones
        addressesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Direcciones direcciones = new Direcciones();
                setVisible(false); // Hide the Admin frame
                direcciones.setVisible(true);
            }
        });

        // Abre GUI para direcciones
        shippingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Envios envios = new Envios();
                setVisible(false); // Hide the Admin frame
                envios.setVisible(true);
            }
        });

        // Añadimos los siguientes elementos a nuestro GUI
        setLayout(new GridLayout(0, 1, 10, 10));
        add(invoicingButton);
        add(shippingButton);
        add(usersButton);
        add(addressesButton);
        add(rutasButton);
        add(MediosTransButton);
    }
}

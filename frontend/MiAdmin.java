package frontend;

import backend.SuperPlantilla;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MiAdmin extends SuperPlantilla {
    private int idCliente;  // Add idCliente field

    // Constructor
    public MiAdmin(int idCliente) {
        super("MiAdmin");
        this.idCliente = idCliente; // Asignar idCliente
        setLayout(new GridLayout(0, 1));

        JButton enviosButton = new JButton("Mis Envíos");
        JButton direccionesButton = new JButton("Mis Direcciones");

        enviosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MisEnvios misEnvios = new MisEnvios(idCliente);
                setVisible(false);
                misEnvios.setVisible(true);
            }
        });

        direccionesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DireccionesCliente direccionesCliente = new DireccionesCliente(idCliente);
                setVisible(false);
                direccionesCliente.setVisible(true);
            }
        });

        setLayout(new GridLayout(0, 1, 10, 10));
        add(enviosButton);
        add(direccionesButton);
    }
}

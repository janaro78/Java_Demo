package frontend;

import backend.SuperPlantilla;
import data.JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddMediosTransporte extends SuperPlantilla {

    private final JTextField textTipoDeTransporte;
    private final JTextField textMatricula;
    private final JTextField textNumContenedor;

    public AddMediosTransporte() {
        super("Añadir Medio de Transporte");
        setLayout(new GridLayout(0, 1, 10, 10));

        textTipoDeTransporte = new JTextField(50);
        textMatricula = new JTextField(30);
        textNumContenedor = new JTextField(40);

        add(showAdminView);

        add(new JLabel("Tipo de Transporte:"));
        add(textTipoDeTransporte);
        add(new JLabel("Matrícula:"));
        add(textMatricula);
        add(new JLabel("Número de Contenedor:"));
        add(textNumContenedor);

        JButton saveMedioTransporte = new JButton("Añadir Medio de Transporte");
        add(saveMedioTransporte);
        saveMedioTransporte.addActionListener(e -> saveMedioTransporteToDB());

        JPanel mediosTransportePanel = new JPanel(new GridLayout(0, 1, 10, 10));
        add(mediosTransportePanel);
        setVisible(true);

        showAdminView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAdminView();
                setVisible(false);
                showAdminView.setVisible(true);
            }
        });
    }

    public void saveMedioTransporteToDB() {
        String tipoDeTransporte = textTipoDeTransporte.getText();
        String matricula = textMatricula.getText();
        Integer numContenedor = Integer.valueOf(textNumContenedor.getText());

        JDBC.insertMediosTransporte(tipoDeTransporte, matricula, numContenedor);

        JOptionPane.showMessageDialog(this, "Medio de Transporte añadido correctamente.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
    }
}

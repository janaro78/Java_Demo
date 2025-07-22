package backend;

import data.JDBC;
import frontend.AddMediosTransporte;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MediosTransporte extends SuperPlantilla {
    private JButton addMediosTransporte = new JButton("Añadir Medio de Transporte");
    private JButton applyMedioTransporteChanges = new JButton("Aplicar cambios en Medio de Transporte");
    private JButton deleteMediosTransporte = new JButton("Borrar Medio de Transporte");
    private JTable mediosTransporteTable;

    public MediosTransporte() {
        super("Medios de Transporte");
        setLayout(new GridLayout(0, 1, 10, 10));

        DefaultTableModel tableModel = new DefaultTableModel();
        mediosTransporteTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(mediosTransporteTable);

        // Nos lleva a panel de administración/inicio
        showAdminView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAdminView();
                setVisible(false); // Oculta ventana Usuarios
                showAdminView.setVisible(true); // Muestra ventana AddUsuarios
            }
        });

        addMediosTransporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddMediosTransporte addMediosTransporte = new AddMediosTransporte();
                setVisible(false);
                addMediosTransporte.setVisible(true);
            }
        });

        applyMedioTransporteChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                callUpdateMediosTransporte();
            }
        });

        deleteMediosTransporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedMediosTransporte();
            }
        });

        add(showAdminView);
        add(addMediosTransporte);
        add(new JLabel("Medios de Transporte:"));
        add(scrollPane);
        add(applyMedioTransporteChanges);
        add(deleteMediosTransporte);

        listMediosTransporte();
        addTableModelListener();
    }
    // Llama al método listMediosTransporte de JDBC para listar los medios de transporte
    private void listMediosTransporte() {
        DefaultTableModel tableModel = JDBC.listMediosTransporte();
        mediosTransporteTable.setModel(tableModel);
    }

    private void addTableModelListener() {
        mediosTransporteTable.getModel().addTableModelListener(e -> {
            AddMediosTransporte addMediosTransporte = new AddMediosTransporte();
            setVisible(false);
            addMediosTransporte.setVisible(true);
        });
    }
    // Llama al método updateMediosTransporte de JDBC para actualizar los datos de la tabla
    private void callUpdateMediosTransporte() {
        DefaultTableModel mediosTransporteTableModel = (DefaultTableModel) mediosTransporteTable.getModel();

        if (mediosTransporteTable.isEditing()) {
            mediosTransporteTable.getCellEditor().stopCellEditing();
        }

        int rowCount = mediosTransporteTable.getRowCount();

        for (int row = 0; row < rowCount; row++) {
            Integer idMedioTransporte = (Integer) mediosTransporteTable.getValueAt(row, 0);
            String tipoDeTransporte = (String) mediosTransporteTable.getValueAt(row, 1);
            String matricula = (String) mediosTransporteTable.getValueAt(row, 2);
            Object objNumContenedor = mediosTransporteTable.getValueAt(row, 3);
            Integer numContenedor = (objNumContenedor == null) ? null : Integer.parseInt(objNumContenedor.toString());

            try {
                // Llama al método updateMediosTransporte de JDBC para actualizar los datos de la tabla
                JDBC.updateMediosTransporte(idMedioTransporte, tipoDeTransporte, matricula, numContenedor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(this, "Cambios efectuados.");
    }

    private void deleteSelectedMediosTransporte() {
        int selectedRow = mediosTransporteTable.getSelectedRow();

        if (selectedRow != -1) {
            int idMedioTransporteToDelete = Integer.parseInt(mediosTransporteTable.getValueAt(selectedRow, 0).toString());

            // Llama al método deleteMediosTransporte de JDBC para borrar el medio de transporte seleccionado
            JDBC.deleteMediosTransporte(idMedioTransporteToDelete);

            // Actualiza la tabla de medios de transporte
            listMediosTransporte();
        } else {
            JOptionPane.showMessageDialog(this, "Se ruega seleccionar medio de transporte para poder borrar.");
        }
    }
}

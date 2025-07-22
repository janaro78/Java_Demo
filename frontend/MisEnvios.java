package frontend;

import backend.SuperPlantilla;
import data.JDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MisEnvios extends SuperPlantilla {
    private JTable tableShipments;
    private JButton showMiAdminView;
    private JButton addMiShipment;
    private JButton applyShipmentChanges;

    public MisEnvios(int idCliente) {
        super("Mis Envios");
        setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();
        tableShipments = new JTable(tableModel);
        tableShipments.setRowHeight(30);

        showMiAdminView = new JButton("Administración");
        addMiShipment = new JButton("Crear envío");
        applyShipmentChanges = new JButton("Aplicar cambios");

        JScrollPane scrollPane = new JScrollPane(tableShipments);
        scrollPane.setPreferredSize(new Dimension(1200, 400));


        // Botón para mostrar la vista de administración
        showMiAdminView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MiAdmin miAdminView = new MiAdmin(idCliente); // Crear nueva instancia de MiAdmin
                setVisible(false); // Oculta ventana Admin
                miAdminView.setVisible(true);
            }
        });
        // Botón para añadir envío
        addMiShipment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddMiEnvios addMiEnvios = new AddMiEnvios(idCliente);
                setVisible(false);
                addMiEnvios.setVisible(true);
            }
        });
        // Botón para aplicar cambios en envíos
        applyShipmentChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                callUpdateShipping();
            }
        });

        add(createButtonPanel(), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setSize(new Dimension(1400, 600));
        setLocationRelativeTo(null);

        listShipmentsForUser(idCliente);
        addTableModelListener();
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.add(showMiAdminView);
        buttonPanel.add(addMiShipment);
        return buttonPanel;
    }

    private void listShipmentsForUser(int idCliente) {
        DefaultTableModel tableModel = JDBC.listShipmentsForUser(idCliente);
        tableShipments.setModel(tableModel);
    }

    private void addTableModelListener() {
        tableShipments.getModel().addTableModelListener(e -> {
            // Capacidad de actualizar datos en la tabla
        });
    }
    // Método para actualizar envíos
    private void callUpdateShipping() {
        DefaultTableModel addressTableModel = (DefaultTableModel) tableShipments.getModel();
        if (tableShipments.isEditing()) {
            tableShipments.getCellEditor().stopCellEditing();
        }
        int rowCount = addressTableModel.getRowCount();

        for (int row = 0; row < rowCount; row++) {
            // Debug información cambiante de addressTableModel
            Object value = addressTableModel.getValueAt(row, 0);
            String columnName = addressTableModel.getColumnName(0);
            System.out.println("  Column: " + columnName + ", Value: " + value + ", Type: " + (value != null ? value.getClass().getName() : "null"));

            // Extract shipment information from the table
            Integer idEnvio = (Integer) tableShipments.getValueAt(row, 0);
            Integer numPaquete = (Integer) tableShipments.getValueAt(row, 1);
            String fechaSalidaString = tableShipments.getValueAt(row, 2).toString();
            String fechaLlegadaString = tableShipments.getValueAt(row, 3).toString();
            String volumenTotal = tableShipments.getValueAt(row, 4).toString();
            String empaquetado = tableShipments.getValueAt(row, 5).toString();
            String tarjetaCredito = tableShipments.getValueAt(row, 6).toString();
            String idEnvioPickup = tableShipments.getValueAt(row, 7).toString();
            String idEnvioDelivery = tableShipments.getValueAt(row, 8).toString();
            String dirUsuario = tableShipments.getValueAt(row, 9).toString();
            Float cantidadImpuestosAduanero = Float.parseFloat(tableShipments.getValueAt(row, 10).toString());
            String rutaEnvio = tableShipments.getValueAt(row, 11).toString();
            String tipoDeTransporte = tableShipments.getValueAt(row, 12).toString();
            String estadoEnvio = tableShipments.getValueAt(row, 13).toString();

            // Call the update method in your JDBC class
            JDBC.updateMiShipping(idEnvio, numPaquete, fechaSalidaString, fechaLlegadaString, volumenTotal, empaquetado,
                    tarjetaCredito, idEnvioPickup, idEnvioDelivery, dirUsuario, cantidadImpuestosAduanero, rutaEnvio, tipoDeTransporte);
        }
    }
    
}

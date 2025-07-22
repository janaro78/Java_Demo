package backend;

import data.JDBC;
import frontend.AddEnvios;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Envios extends SuperPlantilla {
    private JTable tableShipments;
    private JButton showAdminView;
    private JButton addShipment;
    private JButton applyShipmentChanges;
    private JButton deleteShipment;
    private JComboBox<String> pickupTipoViaComboBox;
    private JComboBox<String> deliveryTipoViaComboBox;
    private JComboBox<String> RutaComboBox;
    private JComboBox<String> transporteComboBox;
    private JComboBox<String> estadoEnvioComboBox;

    public Envios() {
        super("Envios");
        setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();
        tableShipments = new JTable(tableModel);
        tableShipments.setRowHeight(30); // Altura de las filas

        showAdminView = new JButton("Administración");
        addShipment = new JButton("Crear envío");
        applyShipmentChanges = new JButton("Aplicar cambios");
        deleteShipment = new JButton("Borrar");

        JScrollPane scrollPane = new JScrollPane(tableShipments);
        scrollPane.setPreferredSize(new Dimension(1200, 400));
        // Botón para mostrar la vista de administración
        showAdminView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAdminView();
                setVisible(false);
                showAdminView.setVisible(true);
            }
        });
        // Botón para añadir envío
        addShipment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddEnvios addEnvios = new AddEnvios();
                setVisible(false);
                addEnvios.setVisible(true);
            }
        });
        // Botón para aplicar cambios en envíos
        applyShipmentChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                callUpdateShipping();
            }
        });
        // Botón para borrar envío
        deleteShipment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedShipping();
            }
        });

        add(createButtonPanel(), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setSize(new Dimension(1400, 600));      // Tamaño de la ventana
        setLocationRelativeTo(null);                        // Centrar ventana

        listShipments();
        addTableModelListener();

        // Pickup ComboBox
        pickupTipoViaComboBox = new JComboBox<>();
        List<String> tipoViaValues = JDBC.getUniqueTipoViaValues();
        pickupTipoViaComboBox.setModel(new DefaultComboBoxModel<>(tipoViaValues.toArray(new String[0])));
        setupComboBoxColumn(tableShipments, 7, pickupTipoViaComboBox);

        // Delivery ComboBox
        deliveryTipoViaComboBox = new JComboBox<>();
        List<String> tipoViaValuesDestino = JDBC.getUniqueTipoViaValues();
        deliveryTipoViaComboBox.setModel(new DefaultComboBoxModel<>(tipoViaValuesDestino.toArray(new String[0])));
        setupComboBoxColumn(tableShipments, 8, deliveryTipoViaComboBox);

        // Nombre de medio de transporte (aire/mar/tierra) ComboBox
        RutaComboBox = new JComboBox<>();
        List<String> descripcionRuta = JDBC.getDescripcionRutaValues();
        RutaComboBox.setModel(new DefaultComboBoxModel<>(descripcionRuta.toArray(new String[0])));
        setupComboBoxColumn(tableShipments, 11, RutaComboBox);

        // ComboBox columna de Transporte
        transporteComboBox = new JComboBox<>();
        List<String> VehiculoTransporte = JDBC.getVehiculoTransporteValues();
        transporteComboBox.setModel(new DefaultComboBoxModel<>(VehiculoTransporte.toArray(new String[0])));
        setupComboBoxColumn(tableShipments, 12, transporteComboBox);  // Ford, Titanic 2, etc.
//        System.out.println("\nLista de Vehiculo Transporte:");
        List<String> vehiculoTransporteList = JDBC.getVehiculoTransporteValues();
//        for (String transporte : vehiculoTransporteList) {
//            System.out.println(transporte);
//        }

        // ComboBox para estadoEnvio
        estadoEnvioComboBox = new JComboBox<>();
        List<String> listaEstadoEnvioValues = JDBC.getlistaEstadoEnvioValues();
        estadoEnvioComboBox.setModel(new DefaultComboBoxModel<>(listaEstadoEnvioValues.toArray(new String[0])));
        setupComboBoxColumn(tableShipments, 13, estadoEnvioComboBox);  // Entregado, pendiente, etc.
//        System.out.println("\nLista de estados de envio de la tabla estados:");
        List<String> listaEstadosBD = JDBC.getlistaEstadoEnvioValues();
//        for (String listaEstados : listaEstadosBD) {
//            System.out.println(listaEstados);
//        }

    }
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.add(showAdminView);
        buttonPanel.add(addShipment);
        buttonPanel.add(applyShipmentChanges);
        buttonPanel.add(deleteShipment);
        return buttonPanel;
    }
    // Método para mostrar la vista de envios
    private void listShipments() {
        DefaultTableModel tableModel = JDBC.listShipments();
        tableShipments.setModel(tableModel);
    }
    // Método para escuchar cambios en la tabla
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
            for (int col = 0; col < addressTableModel.getColumnCount(); col++) {
               // Debug información cambiante de addressTableModel
            Object value = addressTableModel.getValueAt(row, col);
            String columnName = addressTableModel.getColumnName(col);
            System.out.println("  Column: " + columnName + ", Value: " + value + ", Type: " + (value != null ? value.getClass().getName() : "null"));
            }
            String idEnvioPickup = pickupTipoViaComboBox.getSelectedItem().toString();
            String idEnvioDelivery = deliveryTipoViaComboBox.getSelectedItem().toString();
            String estadoEnvio = transporteComboBox.getSelectedItem().toString();
            Integer idEnvio = (Integer) tableShipments.getValueAt(row, 0);                      // Type: java.lang.Integer
            Integer numPaquete = Integer.parseInt(tableShipments.getValueAt(row, 1).toString()); // Type: java.lang.Integer
            String fechaSalidaString = tableShipments.getValueAt(row, 2).toString();            //  Type: string
            String fechaLlegadaString = tableShipments.getValueAt(row, 3).toString();           //  Type: string
            Object volumenTotalObj = addressTableModel.getValueAt(row, 4);
            String strVolumenTotal = (volumenTotalObj instanceof String) ? (String) volumenTotalObj : String.valueOf(volumenTotalObj);
            Object empaquetado = addressTableModel.getValueAt(row, 5);         // Type: java.lang.Float or java.lang.String
            String strEmpaquetado = (empaquetado instanceof String) ? (String) empaquetado : String.valueOf(empaquetado);
            String TarjetaCredito = (String) tableShipments.getValueAt(row, 6);                 // Type: java.lang.String
            idEnvioPickup = (String) tableShipments.getValueAt(row, 7);                        // Type: java.lang.String
            idEnvioDelivery = (String) tableShipments.getValueAt(row, 8);                      // Type: java.lang.String
            String DirUsuario = (String) tableShipments.getValueAt(row, 9);                     // Type: java.lang.String
            Object CantidadImpuestosAduanero = addressTableModel.getValueAt(row, 10);       // Type: java.lang.Float
            Float floatCantidadImpuestosAduanero = (CantidadImpuestosAduanero instanceof Float) ? (Float) CantidadImpuestosAduanero : Float.valueOf(String.valueOf(CantidadImpuestosAduanero));
            String rutaEnvio = RutaComboBox.getSelectedItem().toString();                       // Type: java.lang.String
            rutaEnvio = (String) tableShipments.getValueAt(row, 11);                       // Type: java.lang.String
            String Tipo_de_transporte = transporteComboBox.getSelectedItem().toString();        // Type: java.lang.String
            Tipo_de_transporte = (String) tableShipments.getValueAt(row, 12);        // Type: java.lang.String
            estadoEnvio = (String) tableShipments.getValueAt(row, 13);               // Información sobre el envío

            JDBC.updateShipping(idEnvio, numPaquete, fechaSalidaString, fechaLlegadaString, strVolumenTotal, strEmpaquetado,
                    TarjetaCredito, idEnvioPickup, idEnvioDelivery, DirUsuario, floatCantidadImpuestosAduanero, rutaEnvio, Tipo_de_transporte, estadoEnvio);
            // Debug información estadoEnvio:
//            System.out.println("Updated EstadoEnvio for ID " + idEnvio + " to " + estadoEnvio);
        }
    }
    // Método para borrar envío
    private void deleteSelectedShipping() {
        int selectedRow = tableShipments.getSelectedRow();

        if (selectedRow != -1) {
            int idDireccionToDelete = Integer.parseInt(tableShipments.getValueAt(selectedRow, 0).toString());
            JDBC.deleteShipping(idDireccionToDelete);
            listShipments();
        } else {
            JOptionPane.showMessageDialog(this, "Se ruega seleccionar usuario para poder borrar.");
        }
    }

    // Método para configurar ComboBox
    private void setupComboBoxColumn(JTable table, int column, JComboBox<String> comboBox) {
        TableColumn comboColumn = table.getColumnModel().getColumn(column);

        comboColumn.setCellEditor(new DefaultCellEditor(comboBox));
        comboColumn.setCellRenderer(new ComboBoxCellRenderer(comboBox));
    }
    //  Clase para renderizar el componente
    class ComboBoxCellRenderer implements TableCellRenderer {
        private final JComboBox<String> comboBox;
        // Constructor
        public ComboBoxCellRenderer(JComboBox<String> comboBox) {
            this.comboBox = new JComboBox<>(new DefaultComboBoxModel<>());
            for (int i = 0; i < comboBox.getItemCount(); i++) {
                this.comboBox.addItem(comboBox.getItemAt(i));
            }
        }
        // Renderiza el componente
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            comboBox.setSelectedItem(value);
            return comboBox;
        }
    }
}

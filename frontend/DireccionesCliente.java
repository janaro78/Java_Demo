package frontend;
// Código responsable de mostrar las direcciones de los clientes
// A nivel de métodos es idéntico a Direcciones.java
// el motivo de su creación ha sido para que hubiese un botón que nos llevase a MiAdmin
// de esta forma se evita que el cliente acceda a la administración principal
import backend.SuperPlantilla;
import data.JDBC;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DireccionesCliente extends SuperPlantilla {
    private JButton addAddress = new JButton(("Añadir dirección:"));
    private JButton applyAddressChanges = new JButton("Aplicar cambio en dirección");
    private JButton deleteAddress = new JButton("Borrar dirección");
    private JTable tableAddresses;
    private int idCliente;

    public DireccionesCliente(int idCliente) {
       super("Direcciones para clientes");
        setLayout(new GridLayout(0, 1, 10, 10));
        this.idCliente = idCliente; // Asignar idCliente

        // Formato tabla para mostrar direcciones
        DefaultTableModel tableModel = new DefaultTableModel();
        tableAddresses = new JTable(tableModel);

        for (int i = 0; i < tableAddresses.getColumnCount(); i++) {
            TableColumn column = tableAddresses.getColumnModel().getColumn(i);
            column.setCellEditor(new DefaultCellEditor(new JTextField()));
        }

        JScrollPane scrollPane = new JScrollPane(tableAddresses);

        // Nos lleva a panel de administración/inicio
        showAdminView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MiAdmin miAdminView = new MiAdmin(idCliente); // Crear nueva instancia de MiAdmin
                setVisible(false); // Oculta ventana Admin
                miAdminView.setVisible(true);
            }
        });

        // Añadir una dirección -> Nos lleva a AddAddress
        addAddress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddMiAddress addMiAddress = new AddMiAddress(idCliente);
                setVisible(false); // Oculta ventana actual
                addMiAddress.setVisible(true); // Muestra ventana
            }
        });

        //Aplicar cambios en dirección
        applyAddressChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                callUpdateAddress();
            }
        });

        // Borrar dirección seleccionado
        deleteAddress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedUser();
            }
        });

        // Añadimos los siguientes elementos a nuestro GUI
        add(showAdminView);
        add(addAddress);
        add(new JLabel("Direcciones:"));
        add(scrollPane);
        add(applyAddressChanges);
        add(deleteAddress);

        listAddresses();    // Llama al método para mostrar las direcciones
        addTableModelListener();
    }
    // Mostrar direcciones
    private void listAddresses() {
        DefaultTableModel tableModel = JDBC.listAddresses();
        tableAddresses.setModel(tableModel);
    }
    // Para notificar que hay cambios en la tabla
    private void addTableModelListener() {
        tableAddresses.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
            }
        });
    }

    // Aplicar cambios sobre la dirección seleccionada
    private void callUpdateAddress() {
        DefaultTableModel addressTableModel = (DefaultTableModel) tableAddresses.getModel();

        // Para editar la tabla y validar el cambio realizado.
        if (tableAddresses.isEditing()) {
            tableAddresses.getCellEditor().stopCellEditing();
        }

        int rowCount = addressTableModel.getRowCount();
//        int colCount = addressTableModel.getColumnCount();

        for (int row = 0; row < rowCount; row++) {
            Integer idDireccion = (Integer) addressTableModel.getValueAt(row, 0);
            String TipoVia = (String) addressTableModel.getValueAt(row, 1);
            String NumDomicilio = addressTableModel.getValueAt(row, 2).toString();
            String CodigoPostal = (String) addressTableModel.getValueAt(row, 3);
            String Provincia = (String) addressTableModel.getValueAt(row, 4);
            String País = (String) addressTableModel.getValueAt(row, 5);
            String TipoDomicilio = (String) addressTableModel.getValueAt(row, 6);
            Object ObjTarifaAduanas = addressTableModel.getValueAt(row, 7);
            Float TarifaAduanas = (ObjTarifaAduanas != null ? Float.parseFloat(ObjTarifaAduanas.toString()) : null);
            Object ObjCantidadImpuestosAduanero = addressTableModel.getValueAt(row, 8);
            Float CantidadImpuestosAduanero = (ObjCantidadImpuestosAduanero != null ? Float.parseFloat(ObjCantidadImpuestosAduanero.toString()) : null);
            String Localidad = (String) addressTableModel.getValueAt(row, 9);


//      Debug:
//      System.out.println("Type of direccionOrigen at row " + (row + 1) + ": " + (direccionOrigen != null ? direccionOrigen.getClass().getName() : "null"));

            try {
                // Llama al método en JDBC para actualizar la tabla
                JDBC.updateAddress(idDireccion, TipoVia, NumDomicilio, CodigoPostal, Provincia, País, TipoDomicilio, TarifaAduanas, CantidadImpuestosAduanero, Localidad);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Si hubiese cambios en la tabla, se notificará
        int firstRow = 0;
        int lastRow = rowCount - 1;
        addressTableModel.fireTableRowsUpdated(firstRow, lastRow);

        JOptionPane.showMessageDialog(this, "Cambios realizado con éxito!");
    }



    private void deleteSelectedUser() {
        int selectedRow = tableAddresses.getSelectedRow();

        if (selectedRow != -1) {
            int idDireccionToDelete = Integer.parseInt(tableAddresses.getValueAt(selectedRow, 0).toString());

            // Llama al método disponible en JDBC para borrar la dirección en la id correspondiente.
            JDBC.deleteAddress(idDireccionToDelete);

            // Mostrar direcciones una vez eliminado
            listAddresses();
        // En caso contrario muestra el siguiente mensaje:
        } else {
            JOptionPane.showMessageDialog(this, "Se ruega seleccionar usuario para poder borrar.");
        }
    }
}

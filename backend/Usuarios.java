package backend;
// Cambio rádical en commitChanges

import data.JDBC;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Usuarios extends SuperPlantilla {

    // Elementos de entrar para nuestro GUI
    private JButton addUser = new JButton(("Añadir usuario:"));
    private JButton applyChanges = new JButton("Aplicar edición");
    private JButton deleteUser = new JButton("Borrar usuario");
    private JTable tableUsuarios;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public Usuarios() {
        super("Usuarios");
        setLayout(new GridLayout(0, 1, 10, 10));
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        // Tabla para mostrar los usuarios
        DefaultTableModel tableModel = new DefaultTableModel();
        tableUsuarios = new JTable(tableModel);

        // Habilitar edición de ususarios
        for (int i = 0; i < tableUsuarios.getColumnCount(); i++) {
            TableColumn column = tableUsuarios.getColumnModel().getColumn(i);
            column.setCellEditor(new DefaultCellEditor(new JTextField()));
        }
        JScrollPane scrollPane = new JScrollPane(tableUsuarios);

        // Listener que nos lleva al panel de administración/inicio
        showAdminView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAdminView();
                setVisible(false); // Oculta ventana Usuarios
                showAdminView.setVisible(true); // Muestra ventana AddUsuarios
            }
        });

        // Listener para añadir un usuario nuevo
        addUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddUsuarios addUsuarios = new AddUsuarios();
                setVisible(false); // Oculta ventana Usuarios
                addUsuarios.setVisible(true); // Muestra ventana AddUsuarios
            }
        });

        // Listener para realizar cambios
        applyChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                commitUserChanges();    // Método para realizar los cambios realizados
            }
        });

        deleteUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedUser();   // Método para borrar el usuario seleccionado
            }
        });

        // Añadimos los siguientes elementos a nuestro GUI
        add(showAdminView);
        add(addUser);
        add(new JLabel("Usuarios:"));
        add(scrollPane);
        add(applyChanges);
        add(deleteUser);

        listarUsuarios();
        addTableModelListener();
    }

    // Método para mostrar usuarios
    private void listarUsuarios() {
        DefaultTableModel tableModel = JDBC.obtenerListaUsuarios();
        tableUsuarios.setModel(tableModel);
    }

    // Listener que llama al método que realiza cambios en la tabla
    private void addTableModelListener() {
        tableUsuarios.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
            }
        });
    }

    // Aplicar cambios sobre usuario seleccionado
    private void commitUserChanges() {
        DefaultTableModel tableModel = (DefaultTableModel) tableUsuarios.getModel();
        if (tableUsuarios.isEditing()) {
            tableUsuarios.getCellEditor().stopCellEditing();
        }

        int rowCount = tableModel.getRowCount();

        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
//                Object value = tableModel.getValueAt(row, col);
                String columnName = tableModel.getColumnName(col);
//                System.out.println("  Column: " + columnName + ", Value: " + value + ", Type: " + (value != null ? value.getClass().getName() : "null"));
            }

            Integer idCliente = (Integer) tableModel.getValueAt(row, 0);
            String nombre = (String) tableModel.getValueAt(row, 1);
            String apellido1 = (String) tableModel.getValueAt(row, 2);
            String apellido2 = (String) tableModel.getValueAt(row, 3);
            String dirCorreo = (String) tableModel.getValueAt(row, 4);
            String contraseña = (String) tableModel.getValueAt(row, 5);
            Object fechaAltaObj = tableModel.getValueAt(row, 6);                                                    // Type: java.time.LocalDateTime o Type: java.lang.String
            String strFechaAlta = null;
            if (fechaAltaObj instanceof String) {
                strFechaAlta = (String) fechaAltaObj;
            } else if (fechaAltaObj instanceof LocalDateTime) {
                LocalDateTime fechaAlta = (LocalDateTime) fechaAltaObj;
                strFechaAlta = fechaAlta.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            Object fechaBajaObj = tableModel.getValueAt(row, 7);                                                 // Type: java.time.LocalDateTime o Type: java.lang.String
            String strFechaBaja = null;
            if (fechaBajaObj instanceof String) {
                strFechaBaja = (String) fechaBajaObj;
            } else if (fechaBajaObj instanceof LocalDateTime) {
                LocalDateTime fechaBaja = (LocalDateTime) fechaBajaObj;
                strFechaBaja = fechaBaja.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            String ZonaHoraria = (String) tableModel.getValueAt(row, 8);
            String NombreEmpresa = (String) tableModel.getValueAt(row, 9);
            String idFiscal = (String) tableModel.getValueAt(row, 10);
            String selectUser = (String) tableModel.getValueAt(row, 11);

            try {
                JDBC.updateUsuario(idCliente, nombre, apellido1, apellido2, dirCorreo, contraseña, strFechaAlta, strFechaBaja, ZonaHoraria, NombreEmpresa, idFiscal, selectUser);

            } catch (Exception e) {
                e.printStackTrace();
                // Manejo de excepciones
            }
        }

        int firstRow = 0;
        int lastRow = rowCount - 1;
        tableModel.fireTableRowsUpdated(firstRow, lastRow);

        JOptionPane.showMessageDialog(this, "Cambios realizados!");
    }


    private void deleteSelectedUser() {
        int selectedRow = tableUsuarios.getSelectedRow();

        if (selectedRow != -1) {
            String idClienteToDelete = tableUsuarios.getValueAt(selectedRow, 0).toString();

            // Llama al método disponible en JDBC para borrar al usuario en la id correspondiente.
            JDBC.deleteUsuario(idClienteToDelete);

            // Mostrar usuarios una vez eliminado
            listarUsuarios();
        // En caso contrario muestra el siguiente mensaje:
        } else {
            JOptionPane.showMessageDialog(this, "Se ruega seleccionar usuario para poder borrar.");
        }
    }
}

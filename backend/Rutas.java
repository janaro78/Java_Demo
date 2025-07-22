package backend;

import data.JDBC;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Rutas extends SuperPlantilla {
    private JButton addRuta = new JButton("Añadir ruta");
    private JButton applyRutaChanges = new JButton("Aplicar cambios en ruta");
    private JButton deleteRuta = new JButton("Borrar ruta");
    private JTable tablaRutas;

    public Rutas() {
        super("Rutas");
        setLayout(new GridLayout(0, 1, 10, 10));

        // Formato tabla para mostrar rutas
        DefaultTableModel tableModel = new DefaultTableModel();
        tablaRutas = new JTable(tableModel);

        for (int i = 0; i < tablaRutas.getColumnCount(); i++) {
            TableColumn column = tablaRutas.getColumnModel().getColumn(i);
            column.setCellEditor(new DefaultCellEditor(new JTextField()));
        }

        JScrollPane scrollPane = new JScrollPane(tablaRutas);

        // Nos lleva a panel de administración/inicio
        showAdminView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAdminView();
                setVisible(false); // Oculta ventana Rutas
                showAdminView.setVisible(true); // Muestra ventana AddUsuarios
            }
        });

        // Añadir una ruta -> Nos lleva a AddRuta (replace with the appropriate class)
        addRuta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddRuta addruta = new AddRuta();
                setVisible(false);
                addruta.setVisible(true);
            }
        });

        //Aplicar cambios en ruta
        applyRutaChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                callUpdateRuta();
            }
        });

        // Borrar ruta seleccionado
        deleteRuta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRuta();
            }
        });

        add(showAdminView);
        add(addRuta);
        add(new JLabel("Rutas:"));
        add(scrollPane);
        add(applyRutaChanges);
        add(deleteRuta);

        listRutas();
        addTableModelListener();
    }

    private void listRutas() {
        DefaultTableModel tableModel = JDBC.listRutas(); // Método de enlistar disponible en la capa JDBC
        tablaRutas.setModel(tableModel);
    }

    private void addTableModelListener() {
        tablaRutas.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Pendiente
            }
        });
    }

    // Aplicar cambios sobre la ruta seleccionada
    private void callUpdateRuta() {
        DefaultTableModel rutaTableModel = (DefaultTableModel) tablaRutas.getModel();

        // Para edición en la tabla para validar el cambio correspondiente.
        if (tablaRutas.isEditing()) {
            tablaRutas.getCellEditor().stopCellEditing();
        }

        int rowCount = tablaRutas.getRowCount();

        for (int row = 0; row < rowCount; row++) {
            Integer idruta = (Integer) tablaRutas.getValueAt(row, 0);
            // columna 1 es un float, y a veces se muestra como String...
            Object objCosteRuta = tablaRutas.getValueAt(row, 1);
            Float costeRuta = (objCosteRuta instanceof Float) ? (Float) objCosteRuta : Float.parseFloat((String) objCosteRuta);
            Object objCosteDistancia = tablaRutas.getValueAt(row, 2);
            Float costeDistancia = (objCosteDistancia instanceof Float) ? (Float) objCosteDistancia : Float.parseFloat((String) objCosteDistancia);
            String descripcionRuta = (String) tablaRutas.getValueAt(row, 3);

            // Print inputs for debugging
//            System.out.println("idruta: " + idruta);
//            System.out.println("costeRuta: " + costeRuta);
//            System.out.println("costeDistancia: " + costeDistancia);
//            System.out.println("descripcionRuta: " + descripcionRuta);

            try {
                JDBC.updateRuta(idruta, costeRuta, costeDistancia, descripcionRuta);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(this, "Ruta actualizada.");
    }


    private void deleteSelectedRuta() {
        int selectedRow = tablaRutas.getSelectedRow();

        if (selectedRow != -1) {
            int idRutaToDelete = Integer.parseInt(tablaRutas.getValueAt(selectedRow, 0).toString());

            // Método para borrar ruta seleccionada
            JDBC.deleteSelectedRuta(idRutaToDelete);

            // Mostrar rutas una vez eliminado
            listRutas();
        } else {
            JOptionPane.showMessageDialog(this, "Se ruega seleccionar ruta para poder borrar.");
        }
    }
}

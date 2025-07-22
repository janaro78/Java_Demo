package backend;

import javax.swing.*;
import java.text.SimpleDateFormat;

// Superclase de elementos en común
public class SuperPlantilla extends JFrame {
    //Constructor
    public SuperPlantilla(String title) {
    // GUI
    super(title);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);
    setLocationRelativeTo(null);

    }
    // Método para formatear las fechas
    private SimpleDateFormat createDateTimeFormatter() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    // Método para abrir la ventana de administración/inicio
    public void showAdminView() {
        SwingUtilities.invokeLater(() -> {
            Admin admin = new Admin();
            admin.setVisible(true);
            dispose();
        });
    }
    // Botón para acceder la consola/inicio
    public JButton showAdminView = new JButton(("Administración"));
}
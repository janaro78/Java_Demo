package data;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

// Clase responsable de los métodos para añadir, editar y borrar elementos en los módulos
public class JDBC {
    public static final String URL = "jdbc:mysql://PMYSQL171.dns-servicio.com:3306/10029755_21GIIN";
    public static final String USER = "*****";
    public static final String PASSWORD = "******";

///             M é t o d o s   p a r a   e n v í o s          \\\

    // Método para mostrar todos los envíos
    public static DefaultTableModel listShipments() {
        DefaultTableModel shippingTableModel = new DefaultTableModel();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT e.idEnvio, e.numPaquete, e.fechaSalida, e.fechaLlegada, e.volumenTotal, e.empaquetado, e.tarjetaCredito, " +
                    "pickup.TipoVia AS Origen, " +
                    "delivery.TipoVia AS Destino, " +
                    "u.DirCorreo AS Usuario, " +
                    "d.CantidadImpuestosAduanero AS Coste_Aduanero, " +
                    "r.descripcionRuta AS  DescripcionRuta, " +
                    "m.tipoDeTransporte AS Transporte, " +
                    "es.nombreEstado AS EstadoEnvio " +
                    " FROM envios e " +
                    "LEFT JOIN usuarios u ON e.idCliente = u.idCliente " +
                    "LEFT JOIN direcciones pickup ON e.idEnvioPickup = pickup.idDireccion " +
                    "LEFT JOIN direcciones delivery ON e.idEnvioDelivery = delivery.idDireccion " +
                    "LEFT JOIN direcciones d ON e.idEnvioDelivery = d.idDireccion " +
                    "LEFT JOIN rutas r ON e.idRuta = r.idRuta " +
                    "LEFT JOIN estados es ON e.idEstado = es.idEstado " +
                    "LEFT JOIN medioTransporte m ON e.idMedioTransporte = m.idMedioTransporte ";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Set explicit column names for the table
                Vector<String> columnNames = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    // Use getColumnLabel to get the alias or column name
                    columnNames.add(metaData.getColumnLabel(i));
                }
                shippingTableModel.setColumnIdentifiers(columnNames);

                // Muestra contenido de la tabla
                //System.out.println("Column Names: " + columnNames);

                while (resultSet.next()) {
                    Vector<Object> rowData = new Vector<>();
                    for (int i = 1; i <= columnCount; i++) {
                        Object value = resultSet.getObject(i);
//                        System.out.println("Column: " + metaData.getColumnLabel(i) + ", Value: " + value);
                        rowData.add(value);
                    }
                    shippingTableModel.addRow(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shippingTableModel;
    }
    // Método para obtener la lista de descripciones de estados
    public static List<String> getDescripcionEstados() {
        List<String> estados = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT nombreEstado FROM `10029755_21GIIN`.`estados`";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        estados.add(resultSet.getString("nombreEstado"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estados;
    }
    // Método para insertar envío
    public static void insertShipping(Integer NumPaquete, String fechaSalida, String fechaLlegada, Float VolumenTotal, String empaquetado, String TarjetaCredito, String pickupTipoVia, String deliveryTipoVia, String clienteDirCorreo, String estado) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            int idPickupTipoVia = getIdDireccionesByTipoVia(pickupTipoVia);
            int idDeliveryTipoVia = getIdDireccionesByTipoVia(deliveryTipoVia);
            int idCliente = getIdClienteByDirCorreo(clienteDirCorreo);
            int idEstado = getIdRutaByEstado(estado);

            String sql = "INSERT INTO `10029755_21GIIN`.`envios` (NumPaquete, fechaSalida, fechaLlegada, VolumenTotal, empaquetado, TarjetaCredito, idEnvioPickup, idEnvioDelivery, idCliente, idEstado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, NumPaquete);
                preparedStatement.setString(2, fechaSalida);
                preparedStatement.setString(3, fechaLlegada);
                preparedStatement.setFloat(4, VolumenTotal);
                preparedStatement.setString(5, empaquetado);
                preparedStatement.setString(6, TarjetaCredito);
                preparedStatement.setInt(7, idPickupTipoVia);
                preparedStatement.setInt(8, idDeliveryTipoVia);
                preparedStatement.setInt(9, idCliente);
                preparedStatement.setInt(10, idEstado);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para insertar envío para clientes
    public static void insertMiShipping(Integer NumPaquete, String fechaSalida, String fechaLlegada, Float VolumenTotal, String empaquetado, String TarjetaCredito, String pickupTipoVia, String deliveryTipoVia, Integer idCliente) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            int idPickupTipoVia = getIdDireccionesByTipoVia(pickupTipoVia);
            int idDeliveryTipoVia = getIdDireccionesByTipoVia(deliveryTipoVia);

            String sql = "INSERT INTO `10029755_21GIIN`.`envios` (NumPaquete, fechaSalida, fechaLlegada, VolumenTotal, empaquetado, TarjetaCredito, idEnvioPickup, idEnvioDelivery, idCliente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, NumPaquete);
                preparedStatement.setString(2, fechaSalida);
                preparedStatement.setString(3, fechaLlegada);
                preparedStatement.setFloat(4, VolumenTotal);
                preparedStatement.setString(5, empaquetado);
                preparedStatement.setString(6, TarjetaCredito);
                preparedStatement.setInt(7, idPickupTipoVia);
                preparedStatement.setInt(8, idDeliveryTipoVia);
                preparedStatement.setInt(9, idCliente);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para ofrecer IdRuta mediante el estado
    private static int getIdRutaByEstado(String estado) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT idEstado FROM `10029755_21GIIN`.`estados` WHERE `nombreEstado` = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, estado);
//                System.out.println("Estado en el preparedStatement: " + estado);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("idEstado");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // En caso de error, devuelve un valor por defecto
    }
//    // Método para enlazar la id de la ruta con la descripción de la ruta
//    public static List<String> getDescripcionesRutas() {
//        List<String> descripcionesRutas = new ArrayList<>();
//        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
//            String sql = "SELECT descripcionRuta FROM `10029755_21GIIN`.`rutas`";
//            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//                try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                    while (resultSet.next()) {
//                        descripcionesRutas.add(resultSet.getString("descripcionRuta"));
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return descripcionesRutas;
//    }
    // Método para ofrecer direcciones de origen en combo
    // SELECT idDirección FROM direcciones WHERE TipoVia = x
    // Método para recuperar idDireccion mediante TipoVia
    private static int getIdDireccionesByTipoVia(String tipoVia) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT idDireccion FROM `10029755_21GIIN`.`direcciones` WHERE TipoVia = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, tipoVia);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("idDireccion");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // En caso de error, devuelve un valor por defecto
    }
    // Método para ofrecer direcciones de correo para la idCliente
    private static int getIdClienteByDirCorreo(String dirCorreo) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT idCliente FROM `10029755_21GIIN`.`usuarios` WHERE DirCorreo = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, dirCorreo);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("idCliente");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // En caso de error, devuelve un valor por defecto
    }
    // Método para ofrecer direcciones electrónico en combo
    public static List<String> getAllDirCorreos() {
        List<String> dirCorreos = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT DirCorreo FROM `10029755_21GIIN`.`usuarios`";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String dirCorreo = resultSet.getString("DirCorreo");
                    dirCorreos.add(dirCorreo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dirCorreos;
    }
    // P E N D I E N T E   D E   I M P L E M E N T A R (motivo: casting de la tabla a la BD)  - Método para actualizar datos de envío
    // Método para actualizar los datos de los usuarios en la tabla
// Método para proporcionar idRuta mediante fila de "Coste aduanero"
    private static int getIdRutaByDescripcionRuta(String descripcionRuta) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT idRuta FROM `10029755_21GIIN`.`rutas` WHERE descripcionRuta = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, descripcionRuta);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("idRuta");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // En caso de error, devuelve un valor por defecto
    }
    //                                 1               2            2024-01-05         2024-01-05                 20.0               20.0                5050                   Calle C              Calle VIU            inma@gmail.com          123.66                          Por aire          Ford Transit Connect      Pendiente de recogida
    //   JDBC.updateShipping(Integer idEnvio,         NumPaquete,   fechaSalidaString, fechaLlegadaString,       VolumenTotal,      empaquetado,         TarjetaCredito,        idEnvioPickup,       idEnvioDelivery,       DirUsuario,         CantidadImpuestosAduanero,        rutaEnvio,        Tipo_de_transporte,       nombreEstadoEnvio);
    public static void updateShipping(Integer idEnvio, Integer numPaquete, String fechaSalida, String fechaLlegada, String strVolumenTotal, String empaquetado, String TarjetaCredito, String idEnvioPickup, String idEnvioDelivery, String DirUsuario, Float CantidadImpuestosAduanero, String nombreRuta, String Tipo_de_transporte, String nombreEstadoEnvio) {
        System.out.println("idEnvioPickup @ JDBC: " + idEnvioPickup);
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            int idRuta = getIdRutaByDescripcionRuta(nombreRuta);
//        int idDireccion = getIdDireccionesByCantidadImpuestosAduanero(CantidadImpuestosAduanero);
            int idMedioTransporte = getIdMedioTransporteByTipoDeTransporte(Tipo_de_transporte);
            int idEstado = getIdEstadoByNombreEstado(nombreEstadoEnvio);
            int idDireccionOrigen = getidDireccionByPickupString(idEnvioPickup);
            int idDireccionDestino = getidDireccionByPickupString(idEnvioDelivery);
            int idCliente = getIdClienteByDirCorreo(DirUsuario);

            // Realizar la actualización en la id correspondiente
            String sql = "UPDATE `10029755_21GIIN`.`envios` SET numPaquete=?, fechaSalida=?, fechaLlegada=?, volumenTotal=?, empaquetado=?, TarjetaCredito=?, idEnvioPickup=?, idEnvioDelivery=?, idCliente=?, CantidadImpuestosAduanero=?, idRuta=?, idMedioTransporte=?, idEstado=? WHERE idEnvio=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, numPaquete);
                preparedStatement.setString(2, fechaSalida);
                preparedStatement.setString(3, fechaLlegada);
                preparedStatement.setString(4, strVolumenTotal);
                preparedStatement.setString(5, empaquetado);
                preparedStatement.setString(6, TarjetaCredito);
                preparedStatement.setInt(7, idDireccionOrigen);
                System.out.println("idDireccionOrigen preparestatement@ JDBC: " + idDireccionOrigen);
                preparedStatement.setInt(8, idDireccionDestino);
                preparedStatement.setInt(9, idCliente);                         // idCliente (Usuario mediante DirCorreo)
                preparedStatement.setFloat(10, CantidadImpuestosAduanero);       // idDireccion (mediante CantidadImpuestosAduanero)
                preparedStatement.setInt(11, idRuta);                               // idRuta (Ruta mediante descripción)
                preparedStatement.setInt(12, idMedioTransporte);                 // idMedioTransporte mediante Tipo_de_transporte
                preparedStatement.setInt(13, idEstado);                          // idEstado mediante nombreEstadoEnvio
                preparedStatement.setInt(14, idEnvio);                              // WHERE PK = idEnvio
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("JDBC.updateShipping");
        }
    }
    public static void updateMiShipping(Integer idEnvio, Integer numPaquete, String fechaSalida, String fechaLlegada, String strVolumenTotal, String empaquetado, String TarjetaCredito, String idEnvioPickup, String idEnvioDelivery, String DirUsuario, Float CantidadImpuestosAduanero, String nombreRuta, String Tipo_de_transporte) {
        System.out.println("idEnvioPickup @ JDBC: " + idEnvioPickup);
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            int idRuta = getIdRutaByDescripcionRuta(nombreRuta);
//        int idDireccion = getIdDireccionesByCantidadImpuestosAduanero(CantidadImpuestosAduanero);
            int idMedioTransporte = getIdMedioTransporteByTipoDeTransporte(Tipo_de_transporte);
            int idDireccionOrigen = getidDireccionByPickupString(idEnvioPickup);
            int idDireccionDestino = getidDireccionByPickupString(idEnvioDelivery);
            int idCliente = getIdClienteByDirCorreo(DirUsuario);

            // Realizar la actualización en la id correspondiente para cliente | este método no estará disponible para el cliente
            String sql = "UPDATE `10029755_21GIIN`.`envios` SET numPaquete=?, fechaSalida=?, fechaLlegada=?, volumenTotal=?, empaquetado=?, TarjetaCredito=?, idEnvioPickup=?, idEnvioDelivery=?, idCliente=?, CantidadImpuestosAduanero=?, idRuta=?, idMedioTransporte=? WHERE idEnvio=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, numPaquete);
                preparedStatement.setString(2, fechaSalida);
                preparedStatement.setString(3, fechaLlegada);
                preparedStatement.setString(4, strVolumenTotal);
                preparedStatement.setString(5, empaquetado);
                preparedStatement.setString(6, TarjetaCredito);
                preparedStatement.setInt(7, idDireccionOrigen);
                preparedStatement.setInt(8, idDireccionDestino);
                preparedStatement.setInt(9, idCliente);                         // idCliente (Usuario mediante DirCorreo)
                preparedStatement.setFloat(10, CantidadImpuestosAduanero);       // idDireccion (mediante CantidadImpuestosAduanero)
                preparedStatement.setInt(11, idRuta);                               // idRuta (Ruta mediante descripción)
                preparedStatement.setInt(12, idMedioTransporte);                 // idMedioTransporte mediante Tipo_de_transporte
                preparedStatement.setInt(13, idEnvio);                              // WHERE PK = idEnvio
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("JDBC.updateShipping");
        }
    }
    // Método para recuperar idDireccion mediante TipoVia
    private static int getidDireccionByPickupString(String idEnvioPickup) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){
            String sql = "SELECT idDireccion FROM `10029755_21GIIN`.`direcciones` WHERE TipoVia = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, idEnvioPickup);
                try (ResultSet resultSet = preparedStatement.executeQuery()){
                    if (resultSet.next()){
                        return resultSet.getInt("idDireccion");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // En caso de error, devuelve un valor por defecto
    }
    // Método para ofrecer una **lista** el vehículo de transporte en combo
    public static List<String> getVehiculoTransporteValues() {
        List<String> VehiculoTransporte = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT DISTINCT tipoDeTransporte FROM `10029755_21GIIN`.`medioTransporte`";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String VehiculoSeleccionado = resultSet.getString("tipoDeTransporte");
                    VehiculoTransporte.add(VehiculoSeleccionado); // Añadir vehiculo seleccionado a lista
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return VehiculoTransporte;
    }
    // Método para ofrecer una **lista** el estado de un envío en combo (Pendiente, en tránsito, entregado...)
    public static List<String> getlistaEstadoEnvioValues() {
        List<String> listaEstadoEnvioValues = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT DISTINCT nombreEstado FROM `10029755_21GIIN`.`estados`";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet listaEstados = preparedStatement.executeQuery()) {
                while (listaEstados.next()) {
                    String EstadoEnvio = listaEstados.getString("nombreEstado");
                    listaEstadoEnvioValues.add(EstadoEnvio); // Añadir estado de envío a lista
                }
                // Muestra contenido de la lista de estados de envío
//                System.out.println("\nLista de Estados de Envío:");
//                for (String estadoEnvio : listaEstadoEnvioValues) {
//                    System.out.println(estadoEnvio);
//                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaEstadoEnvioValues;
    }
    // Método para recuperar idEstado mediante nombreEstado
    private static int getIdEstadoByNombreEstado(String nombreEstadoEnvio) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){
            String sql = "SELECT idEstado FROM `10029755_21GIIN`.`estados` WHERE nombreEstado = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, nombreEstadoEnvio);
                try (ResultSet resultSet = preparedStatement.executeQuery()){
                    if (resultSet.next()){
                        int idEstado = resultSet.getInt("idEstado");
//                        System.out.println("ID Estado for " + nombreEstadoEnvio + ": " + idEstado);
                        return idEstado;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("No ID found for " + nombreEstadoEnvio);
        return -1; // En caso de error, devuelve un valor por defecto
    }
    // Método para recuperar idMedioTransporte mediante Tipo_de_transporte
    private static int getIdMedioTransporteByTipoDeTransporte(String tipoDeTransporte) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){
            String sql = "SELECT idMedioTransporte FROM `10029755_21GIIN`.`medioTransporte` WHERE tipoDeTransporte = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, tipoDeTransporte);
                try (ResultSet resultSet = preparedStatement.executeQuery()){
                    if (resultSet.next()){
                        return resultSet.getInt("idMedioTransporte");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // En caso de error, devuelve un valor por defecto
    }
    // Método para recuperar idDireccion mediante CantidadImpuestosAduanero
    private static int getIdDireccionesByCantidadImpuestosAduanero(Float floatCantidadImpuestosAduanero) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT idDireccion FROM `10029755_21GIIN`.`direcciones` WHERE CantidadImpuestosAduanero = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setFloat(1, floatCantidadImpuestosAduanero);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("idDireccion");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // En caso de error, devuelve un valor por defecto
    }
    // Método para borrar un envío
    public static void deleteShipping(Integer idEnvio) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "DELETE FROM `10029755_21GIIN`.`envios` WHERE idEnvio=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idEnvio);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Metodo para obtener la lista de envíos de un usuario
    public static DefaultTableModel listShipmentsForUser(int idCliente) {
        DefaultTableModel shippingTableModel = new DefaultTableModel();
        String sql = "SELECT e.idEnvio, e.numPaquete, e.fechaSalida, e.fechaLlegada, e.volumenTotal, e.empaquetado, e.tarjetaCredito, " +
                "pickup.TipoVia AS Origen, " +
                "delivery.TipoVia AS Destino, " +
                "es.nombreEstado AS Nombre_Estado " +
                "FROM envios e " +
                "LEFT JOIN direcciones pickup ON e.idEnvioPickup = pickup.idDireccion " +
                "LEFT JOIN direcciones delivery ON e.idEnvioDelivery = delivery.idDireccion " +
                "LEFT JOIN estados es ON e.idEstado = es.idEstado " +
                "WHERE e.idCliente = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idCliente);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Nombres de columna explícitos para la tabla
                Vector<String> columnNames = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    // Emplear getColumnLabel para obtener el alias o nombre de columna
                    columnNames.add(metaData.getColumnLabel(i));
                }
                shippingTableModel.setColumnIdentifiers(columnNames);

                while (resultSet.next()) {
                    Vector<Object> rowData = new Vector<>();
                    for (int i = 1; i <= columnCount; i++) {
                        Object value = resultSet.getObject(i);
                        rowData.add(value);
                    }
                    shippingTableModel.addRow(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shippingTableModel;
    }

///     M  é  t  o  d  o  s     p  a  r  a     u  s  u  a  r  i  o  s     \\

    public static ResultSet getUser(String username) {
        ResultSet resultSet = null;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT DirCorreo FROM `10029755_21GIIN`.`usuarios` WHERE DirCorreo = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                resultSet = preparedStatement.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    // Método para insertar un usuario
    public static void insertarUsuario(String nombre, String apellido1, String apellido2, String dirCorreo, String contraseña, String fechaAlta, String fechaBaja, String Zonahoraria, String NombreEmpresa, String idFiscal, String selectUser) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO `10029755_21GIIN`.`usuarios` (Nombre, Apellido1, Apellido2, DirCorreo, Contraseña, FechaAlta, FechaBaja, ZonaHoraria, NombreEmpresa, idFiscal, TipodeUsuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, apellido1);
                preparedStatement.setString(3, apellido2);
                preparedStatement.setString(4, dirCorreo);
                preparedStatement.setString(5, contraseña);
                preparedStatement.setString(6, fechaAlta);
                preparedStatement.setString(7, fechaBaja);
                preparedStatement.setString(8, Zonahoraria);
                preparedStatement.setString(9, NombreEmpresa);
                preparedStatement.setString(10, idFiscal);
                preparedStatement.setString(11, selectUser);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Método para mostrar todos los usuarios
    public static DefaultTableModel obtenerListaUsuarios() {
        DefaultTableModel tableModel = new DefaultTableModel();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM `10029755_21GIIN`.`usuarios`";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Set explicit column names for the table
                Vector<String> columnNames = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    columnNames.add(metaData.getColumnName(i));
                }
                tableModel.setColumnIdentifiers(columnNames);

                while (resultSet.next()) {
                    Vector<Object> rowData = new Vector<>();
                    for (int i = 1; i <= columnCount; i++) {
                        rowData.add(resultSet.getObject(i));
                    }
                    tableModel.addRow(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableModel;
    }
    // Método para borrar una ruta
    public static void deleteUsuario(String idCliente) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "DELETE FROM `10029755_21GIIN`.`usuarios` WHERE idCliente=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, idCliente);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }
    // Método para actualizar los datos de los usuarios en la tabla
    public static void updateUsuario(Integer idCliente, String nombre, String apellido1, String apellido2, String dirCorreo, String contraseña, String fechaAlta, String fechaBaja, String ZonaHoraria, String NombreEmpresa, String idFiscal, String selectUser ) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Realizar la actualización en la id correspondiente
            String sql = "UPDATE `10029755_21GIIN`.`usuarios` SET nombre=?, apellido1=?, apellido2=?, dirCorreo=?, contraseña=?, FechaAlta=?, FechaBaja=?, ZonaHoraria=?, NombreEmpresa=?, idFiscal=?, TipodeUsuario=? WHERE idCliente=?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, apellido1);
                preparedStatement.setString(3, apellido2);
                preparedStatement.setString(4, dirCorreo);
                preparedStatement.setString(5, contraseña);
                preparedStatement.setString(6, fechaAlta);
                preparedStatement.setString(7, fechaBaja);
                preparedStatement.setString(8, ZonaHoraria);
                preparedStatement.setString(9, NombreEmpresa);
                preparedStatement.setString(10, idFiscal);
                preparedStatement.setString(11, selectUser);
                preparedStatement.setInt(12, idCliente);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

///             M é t o d o s   p a r a   d i r e c c i o n e s           \\\

    // Método para actualizar los datos de las direcciones en la tabla
    public static void updateAddress(Integer idDireccion, String TipoVia, String NumDomicilio, String CodigoPostal, String Provincia, String País, String TipoDomicilio, Float TarifaAduanas, Float CantidadImpuestosAduanero, String Localidad) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Realizar la actualización en la id correspondiente
            String sql = "UPDATE `10029755_21GIIN`.`direcciones` SET TipoVia=?, NumDomicilio=?, CodigoPostal=?, Provincia=?, Pais=?, TipoDomicilio=?, TarifaAduanas=?, CantidadImpuestosAduanero=?, Localidad=? WHERE idDireccion=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                // Valores que se mandan a la BD
                preparedStatement.setString(1, TipoVia);
                preparedStatement.setString(2, NumDomicilio);
                preparedStatement.setString(3, CodigoPostal);
                preparedStatement.setString(4, Provincia);
                preparedStatement.setString(5, País);
                preparedStatement.setString(6, TipoDomicilio);
                preparedStatement.setFloat(7, TarifaAduanas);
                preparedStatement.setFloat(8, CantidadImpuestosAduanero);
                preparedStatement.setString(9, Localidad);
                preparedStatement.setInt(10, idDireccion);
                // Ejecutar
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Método para insertar una dirección
    public static void insertAddress(String TipoVia, Integer NumDomicilio, String CodigoPostal, String Provincia, String País, String TipoDomilicio, Float TarifaAduanas, Float CantidadImpuestosAduanero, String Localidad) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO `10029755_21GIIN`.`direcciones` (TipoVia, NumDomicilio, CodigoPostal, Provincia, Pais, TipoDomicilio, TarifaAduanas, CantidadImpuestosAduanero, Localidad) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, TipoVia);
                preparedStatement.setInt(2, NumDomicilio);
                preparedStatement.setString(3, CodigoPostal);
                preparedStatement.setString(4, Provincia);
                preparedStatement.setString(5, País);
                preparedStatement.setString(6, TipoDomilicio);
                preparedStatement.setFloat(7, TarifaAduanas);
                preparedStatement.setFloat(8, CantidadImpuestosAduanero);
                preparedStatement.setString(9, Localidad);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Método para mostrar todas las direcciones
    public static DefaultTableModel listAddresses() {
        DefaultTableModel addressTableModel = new DefaultTableModel();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM `10029755_21GIIN`.`direcciones`";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Set explicit column names for the table
                Vector<String> columnNames = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    columnNames.add(metaData.getColumnName(i));
                }
                addressTableModel.setColumnIdentifiers(columnNames);

                while (resultSet.next()) {
                    Vector<Object> rowData = new Vector<>();
                    for (int i = 1; i <= columnCount; i++) {
                        rowData.add(resultSet.getObject(i));
                    }
                    addressTableModel.addRow(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressTableModel;
    }
    // Método para borrar una dirección
    public static void deleteAddress(Integer idDireccion) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "DELETE FROM `10029755_21GIIN`.`direcciones` WHERE idDireccion=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idDireccion);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Método para ofrecer **lista** direcciones de origen/destino en combo
    public static List<String> getUniqueTipoViaValues() {
        List<String> tipoViaValues = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT DISTINCT TipoVia FROM `10029755_21GIIN`.`direcciones`";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String TipoVia = resultSet.getString("TipoVia");
                    tipoViaValues.add(TipoVia); // Añadir TipoVia a lista
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipoViaValues;
    }
    // Método para ofrecer una **lista** de Medios de transporte (tierra/mar...) en combo
    public static  List<String> getDescripcionRutaValues() {
        List<String> descripcionesRuta = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT DISTINCT descripcionRuta FROM `10029755_21GIIN`.`rutas`";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String descripcionRuta = resultSet.getString("descripcionRuta");
                    descripcionesRuta.add(descripcionRuta); // Add TipoVia to the list
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return descripcionesRuta;
    }

///     M  é  t  o  d  o  s    p  a  r  a    R  u  t  a  s    \\\

    // Método para mostrar todas las rutas
    public static DefaultTableModel listRutas() {
            DefaultTableModel RutasTableModel = new DefaultTableModel();

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "SELECT * FROM `10029755_21GIIN`.`rutas`";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    // Set explicit column names for the table
                    Vector<String> columnNames = new Vector<>();
                    for (int i = 1; i <= columnCount; i++) {
                        columnNames.add(metaData.getColumnName(i));
                    }
                    RutasTableModel.setColumnIdentifiers(columnNames);

                    while (resultSet.next()) {
                        Vector<Object> rowData = new Vector<>();
                        for (int i = 1; i <= columnCount; i++) {
                            rowData.add(resultSet.getObject(i));
                        }
                        RutasTableModel.addRow(rowData);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return RutasTableModel;
        }
    // Método para borrar una ruta
    public static void deleteSelectedRuta(int idRuta) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "DELETE FROM `10029755_21GIIN`.`rutas` WHERE idRuta=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idRuta);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Guardar ruta en base de datos (será llamado desde AddRuta).
    public static void insertRuta(Float costeRuta, Float costeDistancia, String descripcionRuta) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO `10029755_21GIIN`.`rutas` (costeRuta, costeDistancia, descripcionRuta) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setFloat(1, costeRuta);
                preparedStatement.setFloat(2, costeDistancia);
                preparedStatement.setString(3, descripcionRuta);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Método para actualizar datos de ruta
    public static void updateRuta(Integer idruta, Float costeRuta, Float costeDistancia, String descripcionRuta) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Realizar la actualización en la id correspondiente
            String sql = "UPDATE `10029755_21GIIN`.`rutas` SET costeRuta=?, costeDistancia=?, descripcionRuta=? WHERE idRuta=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setFloat(1, costeRuta);
                preparedStatement.setFloat(2, costeDistancia);
                preparedStatement.setString(3, descripcionRuta);
                preparedStatement.setInt(4, idruta);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

///    M é t o d o s   p a r a   M e d i o s   d e   t r a n s p o r t e    \\\

    // Método para mostrar todos los medios de transporte
    public static DefaultTableModel listMediosTransporte() {
        DefaultTableModel mediosTransporteTableModel = new DefaultTableModel();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM `10029755_21GIIN`.`medioTransporte`";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Set explicit column names for the table
                Vector<String> columnNames = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    columnNames.add(metaData.getColumnName(i));
                }
                mediosTransporteTableModel.setColumnIdentifiers(columnNames);

                while (resultSet.next()) {
                    Vector<Object> rowData = new Vector<>();
                    for (int i = 1; i <= columnCount; i++) {
                        rowData.add(resultSet.getObject(i));
                    }
                    mediosTransporteTableModel.addRow(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mediosTransporteTableModel;
    }
    // Método para borrar un medio de transporte
    public static void deleteMediosTransporte(int idMedioTransporte) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "DELETE FROM `10029755_21GIIN`.`medioTransporte` WHERE idMedioTransporte=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idMedioTransporte);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Guardar medio de transporte en base de datos
    public static void insertMediosTransporte(String tipoDeTransporte, String matricula, int numContenedor) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO `10029755_21GIIN`.`medioTransporte` (tipoDeTransporte, matricula, numContenedor) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, tipoDeTransporte);
                preparedStatement.setString(2, matricula);
                preparedStatement.setInt(3, numContenedor);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Método para actualizar datos de medio de transporte
    public static void updateMediosTransporte(Integer idMedioTransporte, String tipoDeTransporte, String matricula, int numContenedor) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){
            String sql = "UPDATE `10029755_21GIIN`.`medioTransporte` SET tipoDeTransporte=?, matricula=?, numContenedor=? WHERE idMedioTransporte=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, tipoDeTransporte);
                preparedStatement.setString(2, matricula);
                preparedStatement.setInt(3, numContenedor);
                preparedStatement.setInt(4, idMedioTransporte);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


///    M é t o d o s   p a r a   F a c t u r a c i ó n    \\\

    // Método para mostrar todas las facturas

    // Método para borrar una factura

    // Guardar factura en base de datos

    // Método para actualizar datos de factura

}
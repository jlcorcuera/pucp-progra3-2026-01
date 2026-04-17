package pe.edu.pucp.progra3.softprog;

import java.sql.*;
import java.util.ResourceBundle;

public class MainReadFromProperties {


    public static void main(String []args) throws SQLException {
        ResourceBundle properties = ResourceBundle.getBundle("db");
        String host = properties.getString("db.host");
        String port = properties.getString("db.port");
        String databaseName = properties.getString("db.database");
        String username = properties.getString("db.user");
        String password = properties.getString("db.password");
        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + databaseName;
        Connection connection = null;
        Statement stm = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            stm = connection.createStatement();
            rs = stm.executeQuery("select now() + INTERVAL 10 year");
            if (rs.next()) {
                System.out.println("Date from server " + rs.getDate(1));
            }
        } catch (Exception ex) {

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}

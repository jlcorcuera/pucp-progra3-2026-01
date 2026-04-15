package pe.edu.pucp.progra3.softprog;

import java.sql.*;

public class MainTryWithResources {

    static String host = "my-softprog-rds.ctjga6kjqpgm.us-east-1.rds.amazonaws.com";
    static Integer port = 3306;
    static String username = "admin";
    static String password = "Admin1234*-";
    static String databaseName = "softprog";
    static String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + databaseName;

    public static void main(String []args) throws SQLException {
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

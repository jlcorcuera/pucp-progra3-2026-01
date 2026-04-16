package pe.edu.pucp.progra3.softprog;

import java.sql.*;

public class MainExampleResultset {

    static String host = "URL HERE";
    static Integer port = 3306;
    static String username = "admin";
    static String password = "Admin1234*-";
    static String databaseName = "softprog";
    static String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + databaseName;

    public static void main(String []args) throws SQLException {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement stm = connection.createStatement();
             ResultSet rs = stm.executeQuery("select id, name from categories")){

             while (rs.next()) {
                 System.out.println(rs.getInt(1) + " " + rs.getString(2));
             }

        }
    }
}

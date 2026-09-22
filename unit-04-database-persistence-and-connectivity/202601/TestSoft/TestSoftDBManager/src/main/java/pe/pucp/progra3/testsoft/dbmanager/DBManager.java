package pe.pucp.progra3.testsoft.dbmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBManager {

    private static String host;
    private static String username;
    private static int port;
    private static String password;
    private static String database;
    private static DBManager instance;

    static {
        // el constructor estatico se ejecuta de forma automatica cuando algun metodo/atributo estatico es referenciado
        ResourceBundle db = ResourceBundle.getBundle("db");
        host = db.getString("db.host");
        port = Integer.parseInt(db.getString("db.port"));
        username = db.getString("db.username");
        password = db.getString("db.password");
        database = db.getString("db.database");
        instance = new DBManager();
    }

    public Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        return  DriverManager.getConnection(url, username, password);
    }

    public static DBManager getInstance() {
        return instance;
    }

}

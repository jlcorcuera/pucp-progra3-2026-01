package pe.edu.pucp.softprog.dao.manager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBManager {

    private static DBManager instance;
    private Properties properties;
    private final String url;
    private final String user;
    private final String password;
    private final String DB_CREDENTIALS_FILE = "db.properties";

    private DBManager() {
        properties = new Properties();
        try{
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DB_CREDENTIALS_FILE);
            properties.load(inputStream);
        }catch(IOException ex){
            throw new RuntimeException("Error when loading properties file: " + ex.getMessage());
        }
        String host = properties.getProperty("host");
        String port = properties.getProperty("port");
        String database = properties.getProperty("database");
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        this.user = properties.getProperty("user");
        this.password = properties.getProperty("password");
    }

    public static DBManager getInstance(){
        if(instance == null)
            instance = new DBManager();
        return instance;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
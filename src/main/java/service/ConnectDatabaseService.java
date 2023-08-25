package service;

import lombok.Getter;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
public class ConnectDatabaseService {
    private Connection connection;

    public ConnectDatabaseService() {
        try {
            URL resource = ConnectDatabaseService.class.getClassLoader().getResource("currency.db");
            String path = null;
            try {
                if (resource != null) {
                    path = new File(resource.toURI()).getAbsolutePath();
                }
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            final String driverName = "org.sqlite.JDBC";
            Class.forName(driverName);
            connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", path));
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
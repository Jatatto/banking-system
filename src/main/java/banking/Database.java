package banking;

import banking.files.FileParser;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class Database extends Thread {

    private Connection connection;
    private Map<String, String> config;

    public Database(File databaseConfiguration)
            throws IOException {

        this.config = FileParser.parseFile(databaseConfiguration);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            openConnection(config);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    private Connection openConnection(Map<String, String> config)
            throws SQLException {

        this.connection = DriverManager.getConnection(
                String.format("jdbc:mysql://%s:%s/%s", config.get("host"), config.get("port"), config.get("database")),
                config.get("user"),
                config.get("password")
        );

        return connection;

    }

    public Connection getConnection()
            throws SQLException {

        if (connection == null || connection.isClosed())
            return openConnection(config);

        return connection;

    }

}

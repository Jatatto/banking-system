package com.jakehonea.banking;

import com.jakehonea.banking.files.FileParser;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class Database extends Thread {

    private final HikariDataSource dataSource;

    public Database(File databaseConfiguration)
            throws IOException {

        Map<String, String> config = FileParser.parseFile(databaseConfiguration);
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl("jdbc:mysql://" + config.get("sql.host") + ":" + config.get("sql.port") + "/" + config.get("sql.database"));
        hikariConfig.setUsername(config.get("sql.username"));
        hikariConfig.setPassword(config.get("sql.password"));

        hikariConfig.setLeakDetectionThreshold(60000);
        hikariConfig.setPoolName("bank-sql");

        dataSource = new HikariDataSource(hikariConfig);

    }

    public Connection openConnection()
            throws SQLException {

        return dataSource.getConnection();

    }

}

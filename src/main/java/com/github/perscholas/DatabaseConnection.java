package com.github.perscholas;

import com.github.perscholas.utils.ConnectionBuilder;
import com.github.perscholas.utils.IOConsole;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by leon on 2/18/2020.
 */
public enum DatabaseConnection implements DatabaseConnectionInterface {
    MANAGEMENT_SYSTEM,
    UAT;

    private static final IOConsole console = new IOConsole(IOConsole.AnsiColor.CYAN);
    private final ConnectionBuilder connectionBuilder;

    DatabaseConnection(ConnectionBuilder connectionBuilder) {
        this.connectionBuilder = connectionBuilder;
    }

    DatabaseConnection() {
        this(new ConnectionBuilder()
                .setUser("root")
                .setPassword("")
                .setPort(3306)
                .setDatabaseVendor("mysql")
                .setHost("127.0.0.1"));
    }

    @Override
    public String getDatabaseName() {
        return name().toLowerCase();
    }

    public String getParams() {
        return "?serverTimezone=UTC&useLegacyDatetimeCode=false";
    }

    @Override
    public Connection getDatabaseConnection() {

        return connectionBuilder
                .setDatabaseName(getDatabaseName())
                .setParams(getParams())
                .build();
    }

    @Override
    public Connection getDatabaseEngineConnection() {
        return connectionBuilder.build();
    }

    @Override
    public void create() {
        String sqlStatement = "CREATE SCHEMA IF NOT EXISTS " + getDatabaseName(); // TODO - define statement instead of null
        String info;
        try {
            // TODO - execute statement
            info = "Successfully executed statement `%s`.";
        } catch (Exception sqlException) {
            info = "Failed to executed statement `%s`.";
        }
        console.println(info, sqlStatement);
    }

    @Override
    public void drop() {
    }

    @Override
    public void use() {
    }

    @Override
    public void executeStatement(String sqlStatement) {
        Connection conn = this.getDatabaseConnection();
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(sqlStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public ResultSet executeQuery(String sqlQuery) {
        Connection conn = this.getDatabaseConnection();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            return resultSet;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
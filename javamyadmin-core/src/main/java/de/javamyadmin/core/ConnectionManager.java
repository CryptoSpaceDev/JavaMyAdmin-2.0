package de.javamyadmin.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.postgresql.jdbc.PgConnection;

public class ConnectionManager {

    private final Connection connection;

    public ConnectionManager(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public void changeDatabase(String databaseName) throws SQLException {
        connection.setCatalog(databaseName);
    }

    public String getCurrentDatabaseName() throws SQLException {
        return connection.getCatalog();
    }

    public List<String> getDatabases() throws SQLException {
        ArrayList<String> databases = new ArrayList<>();

        if (connection instanceof PgConnection pgConn) {
            ResultSet resultSet = pgConn.execSQLQuery("SELECT * FROM pg_database");

            while (resultSet.next()) {
                databases.add(resultSet.getString("datname"));
            }
        } else {
            ResultSet catalogs = connection.getMetaData().getCatalogs();

            while (catalogs.next()) {
                databases.add(catalogs.getString("TABLE_CAT"));
            }
        }

        return databases;
    }

}

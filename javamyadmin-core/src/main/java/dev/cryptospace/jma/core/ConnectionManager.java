package dev.cryptospace.jma.core;

import dev.cryptospace.jma.core.database.ConnectionObject;
import dev.cryptospace.jma.core.database.DatabaseConnection;
import dev.cryptospace.jma.core.database.DatabaseSystem;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.postgresql.jdbc.PgConnection;

public class ConnectionManager extends ConnectionObject {

    private List<DatabaseConnection> databaseConnections;

    public ConnectionManager(@Nonnull String url, @Nonnull String user, String password) throws SQLException {
        super(DriverManager.getConnection(url, user, password));
        connect();
    }

    public ConnectionManager(@Nonnull DatabaseSystem system, @Nonnull String url, @Nonnull String user, String password) throws SQLException {
        super(DriverManager.getConnection(system.createUrl(url), user, password));
        connect();
    }

    private void connect() throws SQLException {
        databaseConnections = new ArrayList<>();

        if (getConnection() instanceof PgConnection pgConn) {
            ResultSet resultSet = pgConn.execSQLQuery("SELECT * FROM pg_database");
            while (resultSet.next()) {
                getConnection().setCatalog(resultSet.getString("datname"));
                databaseConnections.add(new DatabaseConnection(getConnection()));
            }
        } else {
            ResultSet catalogs = getConnection().getMetaData().getCatalogs();
            while (catalogs.next()) {
                getConnection().setCatalog(catalogs.getString("TABLE_CAT"));
                databaseConnections.add(new DatabaseConnection(getConnection()));
            }
        }
    }

    public List<String> getDatabaseNames() throws SQLException {
        List<String> databaseNames = new ArrayList<>();

        if (getConnection() instanceof PgConnection pgConn) {
            ResultSet resultSet = pgConn.execSQLQuery("SELECT * FROM pg_database");
            while (resultSet.next()) {
                databaseNames.add(resultSet.getString("datname"));
            }
        } else {
            ResultSet catalogs = getConnection().getMetaData().getCatalogs();
            while (catalogs.next()) {
                databaseNames.add(catalogs.getString("TABLE_CAT"));
            }
        }

        return databaseNames;
    }

    public List<DatabaseConnection> getDatabaseConnections() {
        return databaseConnections;
    }

}

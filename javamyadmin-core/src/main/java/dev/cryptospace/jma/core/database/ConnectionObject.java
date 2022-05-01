package dev.cryptospace.jma.core.database;

import java.sql.Connection;
import java.sql.SQLException;
import javax.annotation.Nonnull;

public abstract class ConnectionObject {

    private final Connection connection;

    public ConnectionObject(@Nonnull Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

}

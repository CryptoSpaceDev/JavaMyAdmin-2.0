package dev.cryptospace.jma.core.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

public class DatabaseConnection extends ConnectionObject {

    private String databaseName;
    private List<DatabaseTable> databaseTables;

    public DatabaseConnection(@Nonnull Connection connection) throws SQLException {
        super(connection);
        this.databaseName = getConnection().getCatalog();
        this.databaseTables = new ArrayList<>();
        ResultSet resultSet = getConnection().getMetaData().getTables(getConnection().getCatalog(), null, null, null);
        while (resultSet.next()) {
            databaseTables.add(new DatabaseTable(super.getConnection(), resultSet.getString("TABLE_NAME")));
        }
    }

    public void closeConnection() throws SQLException {
        super.closeConnection();
        for (DatabaseTable databaseTable : databaseTables) {
            databaseTable.closeConnection();
        }
        databaseTables.clear();
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public List<DatabaseTable> getDatabaseTables() {
        return databaseTables;
    }

}

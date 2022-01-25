package de.javamyadmin.core.database;

import java.util.List;

public class DatabaseTable {

    private String tableName;
    private final List<DatabaseTableRow> databaseTableRows;

    public DatabaseTable(String tableName, List<DatabaseTableRow> databaseTableRows) {
        this.tableName = tableName;
        this.databaseTableRows = databaseTableRows;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<DatabaseTableRow> getDatabaseTableRows() {
        return databaseTableRows;
    }

}

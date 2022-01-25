package de.javamyadmin.core.database;

import java.util.List;

public class Database {

    private String databaseName;
    private final List<DatabaseTable> databaseTables;

    public Database(String databaseName, List<DatabaseTable> databaseTables) {
        this.databaseName = databaseName;
        this.databaseTables = databaseTables;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public List<DatabaseTable> getDatabaseTables() {
        return databaseTables;
    }

}

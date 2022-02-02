package de.javamyadmin.core.database;

public enum DatabaseSystem {

    MYSQL("MySQL", "jdbc:mysql://"),
    POSTGRESQL("PostgreSQL", "jdbc:postgresql://"),
    SQLITE("SQLite", "jdbc:sqlite://");

    private final String displayName;
    private final String urlPrefix;

    DatabaseSystem(String displayName, String urlPrefix) {
        this.displayName = displayName;
        this.urlPrefix = urlPrefix;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

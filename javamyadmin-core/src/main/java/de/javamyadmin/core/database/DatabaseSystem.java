package de.javamyadmin.core.database;

import javax.annotation.Nonnull;

public enum DatabaseSystem {

    MYSQL("MySQL", "jdbc:mysql://", 3306),
    POSTGRESQL("PostgreSQL", "jdbc:postgresql://", 5432),
    SQLITE("SQLite", "jdbc:sqlite:", null);

    private final String displayName;
    private final String urlPrefix;
    private final Integer defaultPort;

    DatabaseSystem(@Nonnull String displayName, @Nonnull String urlPrefix, Integer defaultPort) {
        this.displayName = displayName;
        this.urlPrefix = urlPrefix;
        this.defaultPort = defaultPort;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public Integer getDefaultPort() {
        return defaultPort;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

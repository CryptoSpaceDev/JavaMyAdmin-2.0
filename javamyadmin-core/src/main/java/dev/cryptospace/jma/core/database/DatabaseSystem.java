package dev.cryptospace.jma.core.database;

import java.util.Objects;
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

    public String createUrl(String ipAddress) {
        String url = "";
        if (Objects.nonNull(defaultPort)) {
            url = urlPrefix.concat(ipAddress).concat(":").concat(defaultPort.toString()).concat("/");
        } else {
            url = (urlPrefix.concat(ipAddress).concat("/"));
        }
        return url;
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
        return getDisplayName();
    }

}

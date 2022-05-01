package dev.cryptospace.jma.core.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.Nonnull;

@SuppressWarnings({"rawtypes"})
public class DatabaseTable extends ConnectionObject {

    private String tableName;
    private ArrayList<DatabaseTableColumn> databaseTableColumns;
    private ArrayList<ArrayList<DatabaseTableCell>> databaseTableRows;

    public DatabaseTable(@Nonnull Connection connection, @Nonnull String tableName) throws SQLException {
        super(connection);
        this.tableName = tableName;
        databaseTableColumns = new ArrayList<>();
        databaseTableRows = new ArrayList<>();
        DatabaseTableColumn databaseTableColumn;
        ArrayList<DatabaseTableCell> databaseTableRow = new ArrayList<>();

        ResultSet rs = getConnection().createStatement().executeQuery(String.format("SELECT * FROM %s;", tableName));

        if (rs.next()) {
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                databaseTableColumn = new DatabaseTableColumn<>(rs.getMetaData().getColumnName(i), rs.getMetaData().getColumnType(i), this);
                databaseTableColumns.add(databaseTableColumn);
                databaseTableRow.add(new DatabaseTableCell<>(databaseTableColumns.get(i - 1), rs.getString(i)));
            }
            databaseTableRows.add(new ArrayList<>(databaseTableRow));
            databaseTableRow.clear();
        }

        while (rs.next()) {
            for (int i = 1; i <= databaseTableColumns.size(); i++) {
                databaseTableRow.add(new DatabaseTableCell<>(databaseTableColumns.get(i - 1), rs.getString(i)));
            }
            databaseTableRows.add(new ArrayList<>(databaseTableRow));
            databaseTableRow.clear();
        }
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return tableName;
    }

}

package de.javamyadmin.core.database;

import java.util.List;

public record DatabaseTableRow(List<DatabaseTableCell<?>> databaseTableCells) {

    public <T> T getValue(DatabaseTableColumn<T> databaseTableColumn) throws Exception {
        for (DatabaseTableCell<?> databaseTableCell : databaseTableCells) {
            if (databaseTableCell.getDatabaseTableColumn().equals(databaseTableColumn)) {
                return databaseTableColumn.valueClass().cast(databaseTableCell.getValue());
            }
        }
        throw new Exception();
    }

}

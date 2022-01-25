package de.javamyadmin.core.database;

public class DatabaseTableCell<T> {

    private final DatabaseTableColumn<T> databaseTableColumn;
    private T value;

    public DatabaseTableCell(DatabaseTableColumn<T> databaseTableColumn, T value) {
        this.databaseTableColumn = databaseTableColumn;
        this.value = value;
    }

    public DatabaseTableColumn<T> getDatabaseTableColumn() {
        return databaseTableColumn;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

}

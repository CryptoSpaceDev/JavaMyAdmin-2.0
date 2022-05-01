package dev.cryptospace.jma.core.database;

public class DatabaseTableCell<T> {

    private T value;

    public DatabaseTableCell(DatabaseTableColumn<T> databaseTableColumn, String value) {
        this.value = databaseTableColumn.convertCell(value);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

}

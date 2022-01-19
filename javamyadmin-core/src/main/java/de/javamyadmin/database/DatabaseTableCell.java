package de.javamyadmin.database;

public class DatabaseTableCell<T> {

    private final DatabaseTableColumn<T> column;
    private T value;

    public DatabaseTableCell(DatabaseTableColumn<T> column, T value) {
        this.column = column;
        this.value = value;
    }

    public DatabaseTableColumn<T> getColumn() {
        return column;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}

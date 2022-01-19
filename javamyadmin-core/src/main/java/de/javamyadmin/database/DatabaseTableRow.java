package de.javamyadmin.database;

import java.util.Arrays;
import java.util.List;

public class DatabaseTableRow {

    private final List<DatabaseTableCell<?>> cells;

    public DatabaseTableRow(DatabaseTableCell<?>... cells) {
        this.cells = Arrays.asList(cells);
    }

    public <T> T getValue(DatabaseTableColumn<T> column) {
        DatabaseTableCell<?> cell = cells.stream()
                                         .filter(c -> c.getColumn().equals(column))
                                         .findFirst()
                                         .orElseThrow(IllegalStateException::new);

        return column.valueClass().cast(cell.getValue());
    }
}

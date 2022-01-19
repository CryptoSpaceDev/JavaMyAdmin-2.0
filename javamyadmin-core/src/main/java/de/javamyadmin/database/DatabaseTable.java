package de.javamyadmin.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseTable {

    // dummy columns
    private final DatabaseTableColumn<String> column1 = new DatabaseTableColumn<>("Test1", String.class);
    private final DatabaseTableColumn<String> column2 = new DatabaseTableColumn<>("Test2", String.class);
    private final DatabaseTableColumn<String> column3 = new DatabaseTableColumn<>("Test3", String.class);

    public List<DatabaseTableColumn<?>> getColumns() {
        return Arrays.asList(column1, column2, column3);
    }

    public List<DatabaseTableRow> getRows() {
        // dummy rows
        List<DatabaseTableRow> rows = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            rows.add(new DatabaseTableRow(
                new DatabaseTableCell<>(column1, "Value" + (i * 3 + 1)),
                new DatabaseTableCell<>(column2, "Value" + (i * 3 + 2)),
                new DatabaseTableCell<>(column3, "Value" + (i * 3 + 3))
            ));
        }

        return rows;
    }
}

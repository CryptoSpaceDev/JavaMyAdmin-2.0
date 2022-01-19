package de.javamyadmin.views;

import java.util.Optional;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import de.javamyadmin.View;
import de.javamyadmin.database.DatabaseTableColumn;
import de.javamyadmin.database.DatabaseTableRow;
import de.javamyadmin.database.DatabaseTable;

public class DataView implements View {

    private final TableView<DatabaseTableRow> view = new TableView<>();
    private final Property<DatabaseTable> tableProperty = new SimpleObjectProperty<>();

    public DataView() {
        initView();
    }

    private void initView() {
        view.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableProperty.addListener((observable, oldValue, newValue) -> refreshTable(newValue));
    }

    private void refreshTable(DatabaseTable newValue) {
        view.getColumns().clear();
        view.getItems().clear();

        if (newValue != null) {
            view.getColumns().addAll(newValue.getColumns().stream().map(this::buildTableColumn).toList());
            view.getItems().addAll(newValue.getRows());
        }
    }

    private <T> TableColumn<DatabaseTableRow, T> buildTableColumn(DatabaseTableColumn<T> column) {
        TableColumn<DatabaseTableRow, T> tableColumn = new TableColumn<>(column.name());
        tableColumn.setCellValueFactory(features -> new SimpleObjectProperty<>(features.getValue().getValue(column)));
        return tableColumn;
    }

    @Override
    public Parent getRoot() {
        return view;
    }

    public Optional<DatabaseTable> getTable() {
        return Optional.ofNullable(tableProperty.getValue());
    }

    public void setTable(DatabaseTable table) {
        tableProperty.setValue(table);
    }
}

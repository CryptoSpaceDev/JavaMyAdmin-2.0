package de.javamyadmin.views;

import de.javamyadmin.View;
import de.javamyadmin.core.database.DatabaseTable;
import de.javamyadmin.core.database.DatabaseTableColumn;
import de.javamyadmin.core.database.DatabaseTableRow;
import java.util.Optional;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataView implements View {

    private static final Logger log = LoggerFactory.getLogger(DataView.class);

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
            // TODO
            // view.getColumns().addAll(newValue.getColumns().stream().map(this::buildTableColumn).toList());
            // view.getItems().addAll(newValue.getRows());
        }
    }

    private <T> TableColumn<DatabaseTableRow, T> buildTableColumn(DatabaseTableColumn<T> column) {
        TableColumn<DatabaseTableRow, T> tableColumn = new TableColumn<>(column.name());
        tableColumn.setCellValueFactory(features -> getColumnValueProperty(column, features));
        return tableColumn;
    }

    private <T> SimpleObjectProperty<T> getColumnValueProperty(DatabaseTableColumn<T> column, CellDataFeatures<DatabaseTableRow, T> features) {
        T value;

        try {
            value = features.getValue().getValue(column);
        } catch (Exception e) {
            log.error("Error while retrieving value for column {}", column.name(), e);
            value = null;
        }

        return new SimpleObjectProperty<>(value);
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

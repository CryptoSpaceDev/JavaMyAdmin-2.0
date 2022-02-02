package de.javamyadmin.views;

import de.javamyadmin.View;
import de.javamyadmin.core.database.DatabaseTable;
import de.javamyadmin.core.database.DatabaseTableRow;
import java.util.Optional;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.scene.control.TableView;

public class DataView implements View {

    private final TableView<DatabaseTableRow> view = new TableView<>();
    private final Property<DatabaseTable> tableProperty = new SimpleObjectProperty<>();

    public DataView() {
        initView();
    }

    private void initView() {
        view.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableProperty.addListener((observable, oldValue, newValue) -> refreshTable());
    }

    private void refreshTable() {
        view.getColumns().clear();
        view.getItems().clear();

        // TODO columns and items
    }

    @Override
    public Parent getView() {
        return view;
    }

    public Optional<DatabaseTable> getTable() {
        return Optional.ofNullable(tableProperty.getValue());
    }

    public void setTable(DatabaseTable table) {
        tableProperty.setValue(table);
    }

}

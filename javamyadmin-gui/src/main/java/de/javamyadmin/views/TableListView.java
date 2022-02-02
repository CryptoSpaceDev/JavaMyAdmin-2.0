package de.javamyadmin.views;

import de.javamyadmin.FontAwesome;
import de.javamyadmin.View;
import de.javamyadmin.core.ConnectionManager;
import de.javamyadmin.data.TableListItem;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableListView implements View {

    private static final Logger log = LoggerFactory.getLogger(TableListView.class);

    private final TreeView<TableListItem> root = new TreeView<>();
    private final TreeItem<TableListItem> rootItem = new TreeItem<>(new TableListItem(FontAwesome.FA_FOLDER, "Root"));
    private final Property<List<String>> databases = new SimpleObjectProperty<>(Collections.emptyList());

    private final ObservableValue<ConnectionManager> connectionManager;

    public TableListView(ObservableValue<ConnectionManager> connectionManager) {
        this.connectionManager = connectionManager;
        initView();
        initProperties();
    }

    private void initView() {
        root.setRoot(rootItem);
        rootItem.setExpanded(true);
        rootItem.setGraphic(rootItem.getValue().icon().build());

        root.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        root.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> onSelectionChange(oldValue, newValue));

        root.focusedProperty().addListener((obs, oldValue, newValue) -> onFocusChange(newValue));
    }

    private void initProperties() {
        databases.addListener((obs, oldValue, newValue) -> {
            rootItem.getChildren().clear();

            for (String database : newValue) {
                TreeItem<TableListItem> item = new TreeItem<>(new TableListItem(FontAwesome.FA_DATABASE, database));
                item.setGraphic(item.getValue().icon().build());
                rootItem.getChildren().add(item);
            }
        });

        connectionManager.addListener((obs, oldValue, newValue) -> refreshDatabasesList(newValue));
        refreshDatabasesList(connectionManager.getValue());
    }

    private void onSelectionChange(TreeItem<TableListItem> oldValue, TreeItem<TableListItem> newValue) {
        if (oldValue != null && oldValue.getGraphic() instanceof Text t) {
            t.setFill(Color.BLACK);
        }

        if (newValue != null && newValue.getGraphic() instanceof Text t) {
            t.setFill(Color.WHITE);
        }
    }

    private void onFocusChange(boolean newValue) {
        if (newValue) {
            root.getSelectionModel().getSelectedItems().forEach(item -> {
                if (item.getGraphic() instanceof Text t) {
                    t.setFill(Color.WHITE);
                }
            });
        } else {
            root.getSelectionModel().getSelectedItems().forEach(item -> {
                if (item.getGraphic() instanceof Text t) {
                    t.setFill(Color.BLACK);
                }
            });
        }
    }

    private void refreshDatabasesList(ConnectionManager newValue) {
        if (newValue == null) {
            databases.setValue(Collections.emptyList());
        } else {
            try {
                databases.setValue(newValue.getDatabases());
            } catch (SQLException e) {
                log.error("Error while getting list of databases", e);
            }
        }
    }

    @Override
    public Parent getRoot() {
        return root;
    }

}

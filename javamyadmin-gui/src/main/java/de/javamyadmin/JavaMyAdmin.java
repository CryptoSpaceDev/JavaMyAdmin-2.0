package de.javamyadmin;

import de.javamyadmin.config.Configuration;
import de.javamyadmin.config.ErrorReporter;
import de.javamyadmin.core.ConnectionManager;
import de.javamyadmin.views.ConnectionManagerView;
import de.javamyadmin.views.DataView;
import de.javamyadmin.views.TableListView;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaMyAdmin extends Application {

    private static final Logger log = LoggerFactory.getLogger(JavaMyAdmin.class);

    private final SimpleObjectProperty<ConnectionManager> connector = new SimpleObjectProperty<>();
    private final Path settingsPath = Path.of("javamyadmin.ini");

    @Override
    public void start(Stage primaryStage) {
        try {
            Configuration.loadFrom(Files.newInputStream(settingsPath), new ErrorReporter.Print());
        } catch (IOException e) {
            log.warn("Could not read from settings file at path {}", settingsPath, e);
        }

        String url = Configuration.DATABASE_URL.getValueOrDefault();
        String user = Configuration.DATABASE_USER.getValueOrDefault();
        String pass = Configuration.DATABASE_PASSWORD.getValueOrDefault();

        if (url == null || user == null || pass == null) {
            showConnectionManagerDialog();
        }

        if (tryConnection(url, user, pass, connector)) {
            return;
        }

        VBox rootPane = new VBox();
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem connectionManagerMenuItem = new MenuItem("Connection Manager", FontAwesome.FA_DATABASE.build());
        connectionManagerMenuItem.setOnAction(event -> showConnectionManagerDialog());
        fileMenu.getItems().addAll(connectionManagerMenuItem);
        menuBar.getMenus().addAll(fileMenu);

        TableListView tableListView = new TableListView(connector);
        DataView dataView = new DataView();

        SplitPane splitPane = new SplitPane(tableListView.getRoot(), dataView.getRoot());
        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.setDividerPosition(0, 0.2);

        rootPane.getChildren().addAll(menuBar, splitPane);

        primaryStage.setScene(new Scene(rootPane, 800.0, 600.0));
        primaryStage.show();
    }

    private void showConnectionManagerDialog() {
        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(new ConnectionManagerView(manager -> {
            connector.setValue(manager);
            dialogStage.close();
        }).getRoot(), 400.0, 300.0));
        dialogStage.showAndWait();
    }

    private boolean tryConnection(String url, String user, String pass, SimpleObjectProperty<ConnectionManager> connector) {
        try {
            connector.setValue(new ConnectionManager(url, user, pass));
        } catch (SQLException e) {
            log.error("Error while connecting to database", e);
            Platform.exit();
            return true;
        }
        return false;
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        try {
            Configuration.saveTo(Files.newOutputStream(settingsPath), new ErrorReporter.Print());
        } catch (IOException e) {
            log.error("Could not write to settings file at path {}", settingsPath, e);
        }
    }

    public static void main(String[] args) {
        log.info("JavaMyAdmin-2.0");
        launch(JavaMyAdmin.class, args);
    }

}

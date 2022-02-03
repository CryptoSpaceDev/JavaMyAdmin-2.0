package de.javamyadmin;

import de.javamyadmin.config.Configuration;
import de.javamyadmin.config.ErrorReporter;
import de.javamyadmin.core.ConnectionManager;
import de.javamyadmin.core.database.DatabaseSystem;
import de.javamyadmin.form.Form;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaMyAdmin extends Application {

    private static final Logger log = LoggerFactory.getLogger(JavaMyAdmin.class);

    private final SimpleObjectProperty<ConnectionManager> connector = new SimpleObjectProperty<>();
    private final Path settingsPath = Path.of("javamyadmin.ini");

    @Override
    public void start(Stage primaryStage) {
        // TODO cleanup this method
        if (primaryStage == null) {
            throw new NullPointerException("primaryStage == null");
        }

        try {
            Configuration.loadFrom(Files.newInputStream(settingsPath), new ErrorReporter.Print());
        } catch (IOException e) {
            log.warn("Could not read from settings file at path {}", settingsPath, e);
        }

        DatabaseSystem system = Configuration.DATABASE_SYSTEM.getValueOrDefault();
        String host = Configuration.DATABASE_HOST.getValueOrDefault();
        Integer port = Configuration.DATABASE_PORT.getValueOrDefault();
        String name = Configuration.DATABASE_NAME.getValueOrDefault();
        String user = Configuration.DATABASE_USER.getValueOrDefault();
        String pass = Configuration.DATABASE_PASS.getValueOrDefault();

        if (system == null || host == null || port == null || name == null || user == null || pass == null) {
            showConnectionManagerDialog(primaryStage);
        }

        if (system == null || host == null || port == null || name == null || user == null || pass == null) {
            Platform.exit();
            return;
        }

        if (tryConnection("%s%s:%d/%s".formatted(system.getUrlPrefix(), host, port, name), user, pass, connector)) {
            return;
        }

        VBox rootPane = new VBox();
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem connectionManagerMenuItem = new MenuItem("Connection Manager", FontAwesome.FA_DATABASE.build());
        connectionManagerMenuItem.setOnAction(event -> showConnectionManagerDialog(primaryStage));
        fileMenu.getItems().addAll(connectionManagerMenuItem);
        menuBar.getMenus().addAll(fileMenu);

        TableListView tableListView = new TableListView(connector);
        DataView dataView = new DataView();

        SplitPane splitPane = new SplitPane(tableListView.getView(), dataView.getView());
        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.setDividerPosition(0, 0.2);

        rootPane.getChildren().addAll(menuBar, splitPane);

        primaryStage.setScene(new Scene(rootPane, 800.0, 600.0));
        primaryStage.show();
    }

    private void showConnectionManagerDialog(@Nonnull Stage owner) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(owner);
        Form connectionManagerForm = new ConnectionManagerView(owner, manager -> {
            connector.setValue(manager);
            dialogStage.close();
        }).getView();
        dialogStage.setScene(new Scene(connectionManagerForm, 600.0, 450.0));
        dialogStage.setResizable(false);
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

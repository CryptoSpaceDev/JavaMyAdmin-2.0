package de.javamyadmin;

import de.javamyadmin.config.Configuration;
import de.javamyadmin.config.ErrorReporter;
import de.javamyadmin.core.ConnectionManager;
import de.javamyadmin.views.DataView;
import de.javamyadmin.views.TableListView;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaMyAdmin extends Application {

    private static final Logger log = LoggerFactory.getLogger(JavaMyAdmin.class);

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
            log.error("Please configure DATABASE_URL, DATABASE_USER and DATABASE_PASSWORD in file 'javamyadmin.ini'");
            Platform.exit();
            return;
        }

        ConnectionManager connector;

        try {
            connector = new ConnectionManager(url, user, pass);
        } catch (SQLException e) {
            log.error("Error while connecting to database", e);
            Platform.exit();
            return;
        }

        TableListView tableListView = new TableListView(connector);

        DataView dataView = new DataView();

        SplitPane rootPane = new SplitPane(tableListView.getRoot(), dataView.getRoot());
        rootPane.setOrientation(Orientation.HORIZONTAL);
        rootPane.setDividerPosition(0, 0.2);

        primaryStage.setScene(new Scene(rootPane, 800.0, 600.0));
        primaryStage.show();
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

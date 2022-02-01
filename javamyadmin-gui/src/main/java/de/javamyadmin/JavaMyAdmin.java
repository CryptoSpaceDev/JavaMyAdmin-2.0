package de.javamyadmin;

import de.javamyadmin.core.ConnectionManager;
import de.javamyadmin.views.DataView;
import de.javamyadmin.views.TableListView;
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

    @Override
    public void start(Stage primaryStage) {
        ConnectionManager connector;

        try {
            connector = new ConnectionManager("jdbc:postgresql://localhost:5433/postgres", "postgres", "postgres");
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

    public static void main(String[] args) {
        log.info("JavaMyAdmin-2.0");
        launch(JavaMyAdmin.class, args);
    }

}

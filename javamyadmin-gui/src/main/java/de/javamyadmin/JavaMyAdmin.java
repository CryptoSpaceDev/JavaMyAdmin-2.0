package de.javamyadmin;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.javamyadmin.database.Connector;
import de.javamyadmin.views.DataView;

public class JavaMyAdmin extends Application {

    private static final Logger log = LoggerFactory.getLogger(JavaMyAdmin.class);

    @Override
    public void start(Stage primaryStage) {
        Connector connector = new Connector();

        DataView view = new DataView();
        view.setTable(connector.getTable());
        Parent root = view.getRoot();
        primaryStage.setScene(new Scene(root, 800.0, 600.0));
        primaryStage.show();
    }

    public static void main(String[] args) {
        log.info("JavaMyAdmin-2.0");
        launch(JavaMyAdmin.class, args);
    }
}

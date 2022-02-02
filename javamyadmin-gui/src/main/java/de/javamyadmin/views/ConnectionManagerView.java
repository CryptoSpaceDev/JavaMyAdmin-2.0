package de.javamyadmin.views;

import de.javamyadmin.FontAwesome;
import de.javamyadmin.View;
import de.javamyadmin.components.ConfigurationTextField;
import de.javamyadmin.config.Configuration;
import de.javamyadmin.core.ConnectionManager;
import java.sql.SQLException;
import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionManagerView implements View {

    private static final Logger log = LoggerFactory.getLogger(ConnectionManagerView.class);

    private final GridPane root = new GridPane();
    private final ConfigurationTextField urlTextField = new ConfigurationTextField(Configuration.DATABASE_URL);
    private final ConfigurationTextField userTextField = new ConfigurationTextField(Configuration.DATABASE_USER);
    private final ConfigurationTextField passwordTextField = new ConfigurationTextField(Configuration.DATABASE_PASSWORD);
    private final Text errorText = new Text();
    private final Button connect = new Button("Connect", FontAwesome.FA_CHECK.build());
    private final Consumer<ConnectionManager> onSubmit;

    public ConnectionManagerView(Consumer<ConnectionManager> onSubmit) {
        this.onSubmit = onSubmit;
        initView();
    }

    private void initView() {
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setMinWidth(100.0);
        root.getColumnConstraints().addAll(column1);

        urlTextField.setPrefColumnCount(Integer.MAX_VALUE);
        userTextField.setPrefColumnCount(Integer.MAX_VALUE);
        passwordTextField.setPrefColumnCount(Integer.MAX_VALUE);

        errorText.setVisible(false);
        errorText.setFill(Color.RED);
        errorText.wrappingWidthProperty().bind(root.widthProperty());

        connect.setOnAction(event -> tryConnection());

        root.setMinSize(400.0, 300.0);

        root.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
        root.setHgap(10.0);
        root.setVgap(5.0);
        root.add(new Label("URL", FontAwesome.FA_LINK.build()), 0, 0);
        root.add(urlTextField, 1, 0);
        root.add(new Label("User", FontAwesome.FA_USER.build()), 0, 1);
        root.add(userTextField, 1, 1);
        root.add(new Label("Password", FontAwesome.FA_KEY.build()), 0, 2);
        root.add(passwordTextField, 1, 2);

        root.add(errorText, 0, 3);
        root.add(connect, 1, 4);
    }

    private void tryConnection() {
        errorText.setVisible(false);

        try {
            ConnectionManager connectionManager = new ConnectionManager(urlTextField.getText(), userTextField.getText(), passwordTextField.getText());
            urlTextField.commit();
            userTextField.commit();
            passwordTextField.commit();
            onSubmit.accept(connectionManager);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            errorText.setVisible(true);
            errorText.setText(e.getMessage());
        }
    }

    @Override
    public Parent getRoot() {
        return root;
    }

}

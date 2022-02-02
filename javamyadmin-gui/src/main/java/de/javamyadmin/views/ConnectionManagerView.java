package de.javamyadmin.views;

import de.javamyadmin.FontAwesome;
import de.javamyadmin.View;
import de.javamyadmin.config.Configuration;
import de.javamyadmin.core.ConnectionManager;
import de.javamyadmin.core.database.DatabaseSystem;
import de.javamyadmin.form.Form;
import de.javamyadmin.form.FormComboBox;
import de.javamyadmin.form.FormIntegerPicker;
import de.javamyadmin.form.FormPasswordField;
import de.javamyadmin.form.FormTextField;
import de.javamyadmin.form.InvalidValueException;
import de.javamyadmin.utils.BindingUtils;
import de.javamyadmin.utils.ComponentUtils;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.beans.property.SimpleStringProperty;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionManagerView implements View {

    private static final Logger log = LoggerFactory.getLogger(ConnectionManagerView.class);

    private final Form form = new Form();
    private final FormComboBox<DatabaseSystem> system = new FormComboBox<>(Configuration.DATABASE_SYSTEM, DatabaseSystem.class, "System",
        FontAwesome.FA_DATABASE.build());
    private final FormTextField host = new FormTextField(Configuration.DATABASE_HOST, "Host", FontAwesome.FA_LINK.build());
    private final FormIntegerPicker port = new FormIntegerPicker(Configuration.DATABASE_PORT, 0, 65535, 1, "Port", FontAwesome.FA_UNLOCK.build());
    private final FormTextField name = new FormTextField(Configuration.DATABASE_NAME, "Database", FontAwesome.FA_ALIGN_LEFT.build());
    private final FormTextField url = new FormTextField(null, "URL", FontAwesome.FA_NETWORK_WIRED.build());
    private final FormTextField user = new FormTextField(Configuration.DATABASE_USER, "User", FontAwesome.FA_USER.build());
    private final FormPasswordField password = new FormPasswordField(Configuration.DATABASE_PASS, "Password", FontAwesome.FA_KEY.build());

    public ConnectionManagerView(@Nonnull Consumer<ConnectionManager> onSubmit) {
        Objects.requireNonNull(onSubmit);
        initView();

        form.addSubmitListener(() -> {
            try {
                ConnectionManager manager = new ConnectionManager(
                    url.getTextField().getText(),
                    Configuration.DATABASE_USER.getValueOrDefault(),
                    Configuration.DATABASE_PASS.getValueOrDefault()
                );

                onSubmit.accept(manager);
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    private void initView() {
        form.add(system);
        form.addSeparator();
        form.add(host);
        form.add(port);
        form.add(name);
        form.add(url);
        form.addSeparator();
        form.add(user);
        form.add(password);

        system.getNode().getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.getDefaultPort() != null) {
                port.getNode().getValueFactory().setValue(newValue.getDefaultPort());
            }
        });

        ComponentUtils.addTextFieldHostValidator(host.getTextField());

        url.getCopyButtonVisibleProperty().setValue(true);
        url.getTextField().setDisable(true);
        url.getTextField().textProperty().bind(BindingUtils.concatStrings(true,
            BindingUtils.mapString(system.getNode().valueProperty(), DatabaseSystem::getUrlPrefix),
            host.getTextField().textProperty(),
            new SimpleStringProperty(":"),
            port.getNode().valueProperty().asString(),
            new SimpleStringProperty("/"),
            BindingUtils.urlSafeStringBinding(name.getTextField().textProperty())
        ));

        password.getShowPasswordButtonVisibleProperty().setValue(true);

        form.getSubmitButton().setText("Connect");
        form.getSubmitButton().setGraphic(FontAwesome.FA_SIGN_IN_ALT.build());
        form.addVerifier(this::tryConnection);
    }

    private void tryConnection() throws InvalidValueException {
        ConnectionManager manager = null;

        try {
            manager = new ConnectionManager(url.getTextField().getText(), user.getTextField().getText(), password.getPasswordProperty().get());
        } catch (SQLException e) {
            throw new InvalidValueException(host, e.getMessage(), e);
        } finally {
            if (manager != null) {
                try {
                    manager.closeConnection();
                } catch (SQLException ignore) {
                    // not needed
                }
            }
        }
    }

    @Override
    public Form getView() {
        return form;
    }

}

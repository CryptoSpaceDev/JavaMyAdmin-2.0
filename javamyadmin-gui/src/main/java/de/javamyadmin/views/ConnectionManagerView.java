package de.javamyadmin.views;

import de.javamyadmin.FontAwesome;
import de.javamyadmin.View;
import de.javamyadmin.config.Configuration;
import de.javamyadmin.core.ConnectionManager;
import de.javamyadmin.core.database.DatabaseSystem;
import de.javamyadmin.form.Form;
import de.javamyadmin.form.FormComboBox;
import de.javamyadmin.form.FormFileChooser;
import de.javamyadmin.form.FormIntegerPicker;
import de.javamyadmin.form.FormPasswordField;
import de.javamyadmin.form.FormTextField;
import de.javamyadmin.form.InvalidValueException;
import de.javamyadmin.utils.ComponentUtils;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.beans.binding.Bindings;
import javafx.stage.Stage;
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
    private final FormFileChooser file;

    public ConnectionManagerView(@Nonnull Stage owner, @Nonnull Consumer<ConnectionManager> onSubmit) {
        Objects.requireNonNull(owner);
        Objects.requireNonNull(onSubmit);
        file = new FormFileChooser(Configuration.DATABASE_FILE, owner, "File", FontAwesome.FA_FILE_ALT.build());
        initView();

        form.addSubmitListener(() -> {
            try {
                ConnectionManager manager = new ConnectionManager(
                    url.getValueProperty().getValue(),
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
        refreshForm();

        system.getNode().getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            refreshForm();

            if (newValue != null && newValue.getDefaultPort() != null) {
                port.getNode().getValueFactory().setValue(newValue.getDefaultPort());
            }
        });

        ComponentUtils.addTextFieldHostValidator(host.getTextField());

        url.getCopyButtonVisibleProperty().setValue(true);
        url.getTextField().setDisable(true);
        url.getTextField().textProperty().bind(Bindings.createStringBinding(this::createUrl,
            system.getValueProperty(),
            host.getValueProperty(),
            port.getValueProperty(),
            name.getValueProperty(),
            file.getValueProperty()
        ));

        password.getShowPasswordButtonVisibleProperty().setValue(true);

        form.getSubmitButton().setText("Connect");
        form.getSubmitButton().setGraphic(FontAwesome.FA_SIGN_IN_ALT.build());
        form.addVerifier(this::tryConnection);
    }

    private void refreshForm() {
        form.clearNodes();
        form.add(system);
        form.addSeparator();

        if (system.getValueProperty().getValue() == DatabaseSystem.SQLITE) {
            form.add(file);
            form.addPlaceholder();
            form.addPlaceholder();
        } else {
            form.add(host);
            form.add(port);
            form.add(name);
        }

        form.add(url);

        if (system.getValueProperty().getValue() != DatabaseSystem.SQLITE) {
            form.addSeparator();
            form.add(user);
            form.add(password);
        }
    }

    private void tryConnection() throws InvalidValueException {
        ConnectionManager manager = null;

        try {
            manager = new ConnectionManager(url.getTextField().getText(), user.getTextField().getText(), password.getValueProperty().get());
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

    private String createUrl() {
        DatabaseSystem databaseSystem = system.getValueProperty().getValue();
        String prefix = databaseSystem.getUrlPrefix();
        String domain;

        if (databaseSystem == DatabaseSystem.SQLITE) {
            domain = file.getValueProperty().getValue();
        } else {
            domain = "%s:%d/%s".formatted(
                host.getValueProperty().getValue(),
                port.getValueProperty().getValue(),
                name.getValueProperty().getValue() == null ? "" : URLEncoder.encode(name.getValueProperty().getValue(), StandardCharsets.UTF_8));
        }

        return prefix.concat(domain);
    }

    @Override
    public Form getView() {
        return form;
    }

}

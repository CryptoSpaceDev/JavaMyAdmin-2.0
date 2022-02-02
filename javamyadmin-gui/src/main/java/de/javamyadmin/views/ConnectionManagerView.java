package de.javamyadmin.views;

import de.javamyadmin.FontAwesome;
import de.javamyadmin.View;
import de.javamyadmin.config.Configuration;
import de.javamyadmin.core.ConnectionManager;
import de.javamyadmin.form.Form;
import de.javamyadmin.form.FormTextField;
import de.javamyadmin.form.InvalidValueException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionManagerView implements View {

    private static final Logger log = LoggerFactory.getLogger(ConnectionManagerView.class);

    private final Form form = new Form();
    private final FormTextField url = new FormTextField(Configuration.DATABASE_URL, "URL", FontAwesome.FA_LINK.build());
    private final FormTextField user = new FormTextField(Configuration.DATABASE_USER, "User", FontAwesome.FA_USER.build());
    private final FormTextField password = new FormTextField(Configuration.DATABASE_PASS, "Password", FontAwesome.FA_KEY.build());

    public ConnectionManagerView(@Nonnull Consumer<ConnectionManager> onSubmit) {
        Objects.requireNonNull(onSubmit);
        initView();

        form.addSubmitListener(() -> {
            try {
                ConnectionManager manager = new ConnectionManager(
                    Configuration.DATABASE_URL.getValueOrDefault(),
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
        form.add(url);
        form.add(user);
        form.add(password);

        form.addVerifier(this::tryConnection);
    }

    private void tryConnection() throws InvalidValueException {
        ConnectionManager manager = null;

        try {
            manager = new ConnectionManager(url.getNode().getText(), user.getNode().getText(), password.getNode().getText());
        } catch (SQLException e) {
            throw new InvalidValueException(url, e.getMessage(), e);
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

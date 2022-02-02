package de.javamyadmin.form;

import de.javamyadmin.config.ConfigurationParameter;
import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.annotation.Nonnull;

public class FormPasswordField implements FormNode {

    private final ConfigurationParameter<String> parameter;
    private final Label label = new Label();
    private final PasswordField passwordField = new PasswordField();

    public FormPasswordField(@Nonnull ConfigurationParameter<String> parameter) {
        this(parameter, parameter.getKey());
    }

    public FormPasswordField(@Nonnull ConfigurationParameter<String> parameter, String label) {
        this(parameter, label, null);
    }

    public FormPasswordField(@Nonnull ConfigurationParameter<String> parameter, String message, Node graphic) {
        this.parameter = Objects.requireNonNull(parameter);
        label.setText(message);
        label.setGraphic(graphic);
        passwordField.setText(parameter.getValueOrDefault());
        passwordField.setPrefColumnCount(Integer.MAX_VALUE);
    }

    @Override
    public Label getLabel() {
        return label;
    }

    @Override
    public TextField getNode() {
        return passwordField;
    }

    @Override
    public void verify() throws InvalidValueException {
        // always valid
    }

    @Override
    public void commit() {
        parameter.setValue(passwordField.getText());
    }

    @Override
    public void rollback() {
        passwordField.setText(parameter.getValueOrDefault());
    }

}

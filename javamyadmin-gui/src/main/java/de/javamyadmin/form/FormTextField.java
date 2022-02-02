package de.javamyadmin.form;

import de.javamyadmin.config.ConfigurationParameter;
import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.annotation.Nonnull;

public class FormTextField implements FormNode {

    private final ConfigurationParameter<String> parameter;
    private final Label label = new Label();
    private final TextField textField = new TextField();

    public FormTextField(@Nonnull ConfigurationParameter<String> parameter) {
        this(parameter, parameter.getKey());
    }

    public FormTextField(@Nonnull ConfigurationParameter<String> parameter, String label) {
        this(parameter, label, null);
    }

    public FormTextField(@Nonnull ConfigurationParameter<String> parameter, String message, Node graphic) {
        this.parameter = Objects.requireNonNull(parameter);
        label.setText(message);
        label.setGraphic(graphic);
        textField.setText(parameter.getValueOrDefault());
        textField.setPrefColumnCount(Integer.MAX_VALUE);
    }

    @Override
    public Label getLabel() {
        return label;
    }

    @Override
    public TextField getNode() {
        return textField;
    }

    @Override
    public void verify() throws InvalidValueException {
        // always valid
    }

    @Override
    public void commit() {
        parameter.setValue(textField.getText());
    }

    @Override
    public void rollback() {
        textField.setText(parameter.getValueOrDefault());
    }

}

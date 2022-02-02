package de.javamyadmin.form;

import de.javamyadmin.config.ConfigurationParameter;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FormTextField implements FormNode {

    private final ConfigurationParameter<String> parameter;
    private final Label label = new Label();
    private final TextField textField = new TextField();

    public FormTextField(ConfigurationParameter<String> parameter) {
        this(parameter, parameter == null ? "" : parameter.getKey());
    }

    public FormTextField(ConfigurationParameter<String> parameter, String label) {
        this(parameter, label, null);
    }

    public FormTextField(ConfigurationParameter<String> parameter, String message, Node graphic) {
        this.parameter = parameter;
        label.setText(message);
        label.setGraphic(graphic);
        textField.setText(parameter == null ? "" : parameter.getValueOrDefault());
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
    public void verify() {
        // always valid
    }

    @Override
    public void commit() {
        if (parameter != null) {
            parameter.setValue(textField.getText());
        }
    }

    @Override
    public void rollback() {
        if (parameter != null) {
            textField.setText(parameter.getValueOrDefault());
        }
    }

}

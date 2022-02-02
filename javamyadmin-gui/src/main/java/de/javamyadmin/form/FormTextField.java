package de.javamyadmin.form;

import de.javamyadmin.FontAwesome;
import de.javamyadmin.config.ConfigurationParameter;
import java.util.Collections;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.HBox;

public class FormTextField implements FormNode {

    private final ConfigurationParameter<String> parameter;
    private final Label label = new Label();
    private final HBox textFieldLayout = new HBox();
    private final TextField textField = new TextField();
    private final Button copyValueButton = new Button("", FontAwesome.FA_COPY.build());

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

        textFieldLayout.getChildren().addAll(textField);

        copyValueButton.setTooltip(new Tooltip("Copy URL"));
        copyValueButton.visibleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == Boolean.TRUE) {
                textFieldLayout.getChildren().add(copyValueButton);
            } else {
                textFieldLayout.getChildren().remove(copyValueButton);
            }
        });
        copyValueButton.setVisible(false);

        copyValueButton.setOnAction(
            event -> Clipboard.getSystemClipboard().setContent(Collections.singletonMap(DataFormat.URL, textField.getText())));
    }

    @Override
    public Label getLabel() {
        return label;
    }

    @Override
    public Parent getNode() {
        return textFieldLayout;
    }

    public TextField getTextField() {
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

    public BooleanProperty getCopyButtonVisibleProperty() {
        return copyValueButton.visibleProperty();
    }

}

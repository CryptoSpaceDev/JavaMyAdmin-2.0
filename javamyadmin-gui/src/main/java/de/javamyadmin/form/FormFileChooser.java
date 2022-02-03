package de.javamyadmin.form;

import de.javamyadmin.FontAwesome;
import de.javamyadmin.config.ConfigurationParameter;
import java.io.File;
import java.nio.file.Path;
import javafx.beans.value.ObservableStringValue;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FormFileChooser implements FormNode {

    private final ConfigurationParameter<Path> parameter;
    private final Label label = new Label();
    private final HBox textFieldLayout = new HBox();
    private final TextField textField = new TextField();
    private final Button pickFileButton = new Button("", FontAwesome.FA_ELLIPSIS_H.build());

    public FormFileChooser(ConfigurationParameter<Path> parameter, Stage owner) {
        this(parameter, owner, parameter == null ? "" : parameter.getKey());
    }

    public FormFileChooser(ConfigurationParameter<Path> parameter, Stage owner, String label) {
        this(parameter, owner, label, null);
    }

    public FormFileChooser(ConfigurationParameter<Path> parameter, Stage owner, String message, Node graphic) {
        this.parameter = parameter;
        label.setText(message);
        label.setGraphic(graphic);
        textField.setText(parameter == null ? "" : String.valueOf(parameter.getValueOrDefault()));
        textField.setPrefColumnCount(Integer.MAX_VALUE);
        pickFileButton.setTooltip(new Tooltip("Choose File"));
        pickFileButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(owner);

            if (file == null) {
                textField.setText("");
            } else {
                textField.setText(String.valueOf(file.toPath().toAbsolutePath()));
            }
        });

        textFieldLayout.getChildren().addAll(textField, pickFileButton);
    }

    @Override
    public Label getLabel() {
        return label;
    }

    @Override
    public Parent getNode() {
        return textFieldLayout;
    }

    @Override
    public void verify() {
        // always valid
    }

    @Override
    public void commit() {
        if (parameter != null) {
            parameter.setValue(Path.of(textField.getText()));
        }
    }

    @Override
    public void rollback() {
        if (parameter != null) {
            textField.setText(String.valueOf(parameter.getValueOrDefault()));
        }
    }

    public ObservableStringValue getValueProperty() {
        return textField.textProperty();
    }

}

package de.javamyadmin.form;

import de.javamyadmin.FontAwesome;
import de.javamyadmin.config.ConfigurationParameter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class FormPasswordField implements FormNode {

    private final ConfigurationParameter<String> parameter;
    private final Label label = new Label();
    private final HBox nodeLayout = new HBox();
    private final TextField passwordVisibleField = new TextField();
    private final PasswordField passwordHiddenField = new PasswordField();
    private final ToggleButton showPasswordButton = new ToggleButton("", FontAwesome.FA_EYE.build());
    private final StringProperty passwordProperty = new SimpleStringProperty();

    public FormPasswordField(ConfigurationParameter<String> parameter) {
        this(parameter, parameter == null ? "" : parameter.getKey());
    }

    public FormPasswordField(ConfigurationParameter<String> parameter, String label) {
        this(parameter, label, null);
    }

    public FormPasswordField(ConfigurationParameter<String> parameter, String message, Node graphic) {
        this.parameter = parameter;
        label.setText(message);
        label.setGraphic(graphic);
        passwordProperty.setValue(parameter == null ? "" : parameter.getValueOrDefault());

        passwordVisibleField.setPrefColumnCount(Integer.MAX_VALUE);
        passwordHiddenField.setPrefColumnCount(Integer.MAX_VALUE);
        passwordHiddenField.textProperty().bindBidirectional(passwordProperty);

        StackPane passwordTextFieldsContainer = new StackPane(passwordHiddenField);
        nodeLayout.getChildren().add(passwordTextFieldsContainer);

        showPasswordButton.setTooltip(new Tooltip("Show Password"));
        showPasswordButton.visibleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == Boolean.TRUE) {
                nodeLayout.getChildren().add(showPasswordButton);
            } else {
                nodeLayout.getChildren().remove(showPasswordButton);
            }
        });
        showPasswordButton.setVisible(false);

        showPasswordButton.setOnAction(event -> {
            if (showPasswordButton.isSelected()) {
                passwordTextFieldsContainer.getChildren().clear();
                passwordTextFieldsContainer.getChildren().add(passwordVisibleField);
                passwordHiddenField.textProperty().unbindBidirectional(passwordProperty);
                passwordVisibleField.textProperty().bindBidirectional(passwordProperty);
                showPasswordButton.setGraphic(FontAwesome.FA_EYE_SLASH.build());
                passwordHiddenField.setTooltip(new Tooltip("Hide Password"));
            } else {
                passwordTextFieldsContainer.getChildren().clear();
                passwordTextFieldsContainer.getChildren().add(passwordHiddenField);
                passwordHiddenField.textProperty().bindBidirectional(passwordProperty);
                passwordVisibleField.textProperty().unbindBidirectional(passwordProperty);
                showPasswordButton.setGraphic(FontAwesome.FA_EYE.build());
                passwordHiddenField.setTooltip(new Tooltip("Show Password"));
            }
        });
    }

    @Override
    public Label getLabel() {
        return label;
    }

    @Override
    public Parent getNode() {
        return nodeLayout;
    }

    @Override
    public void verify() throws InvalidValueException {
        // always valid
    }

    @Override
    public void commit() {
        if (parameter != null) {
            parameter.setValue(passwordHiddenField.getText());
        }
    }

    @Override
    public void rollback() {
        if (parameter != null) {
            passwordHiddenField.setText(parameter.getValueOrDefault());
        }
    }

    public BooleanProperty getShowPasswordButtonVisibleProperty() {
        return showPasswordButton.visibleProperty();
    }

    public StringProperty getPasswordProperty() {
        return passwordProperty;
    }

}

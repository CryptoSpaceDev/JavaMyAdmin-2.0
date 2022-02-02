package de.javamyadmin.components;

import java.util.Objects;
import javafx.beans.property.Property;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class LabelTextFieldGroup extends BorderPane {

    private final Label label;
    private final TextField textField;

    public LabelTextFieldGroup(String label) {
        this(new Label(label), new TextField());
    }

    public LabelTextFieldGroup(String label, TextField textField) {
        this(new Label(label), textField);
    }

    public LabelTextFieldGroup(Label label, TextField textField) {
        this.label = Objects.requireNonNull(label);
        this.textField = Objects.requireNonNull(textField);

        setLeft(label);
        setRight(textField);
    }

    public Property<String> getLabelProperty() {
        return label.textProperty();
    }

    public Property<String> getTextFieldValueProperty() {
        return textField.textProperty();
    }

}

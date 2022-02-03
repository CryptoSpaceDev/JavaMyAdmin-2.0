package de.javamyadmin.form;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FormPlaceholder implements FormNode {

    private final Label label = new Label();
    private final TextField textField = new TextField();

    public FormPlaceholder() {
        textField.setVisible(false);
    }

    @Override
    public Node getLabel() {
        return label;
    }

    @Override
    public Node getNode() {
        return textField;
    }

    @Override
    public void verify() {
        // always valid
    }

    @Override
    public void commit() {
        // nothing to commit
    }

    @Override
    public void rollback() {
        // nothing to rollback
    }

}

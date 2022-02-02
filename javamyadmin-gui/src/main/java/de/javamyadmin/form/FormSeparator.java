package de.javamyadmin.form;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Separator;

public class FormSeparator implements FormNode {

    @Override
    public Node getLabel() {
        return new Separator(Orientation.HORIZONTAL);
    }

    @Override
    public Node getNode() {
        return new Separator(Orientation.HORIZONTAL);
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

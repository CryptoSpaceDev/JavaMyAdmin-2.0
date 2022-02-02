package de.javamyadmin.form;

import javafx.scene.Node;

public interface FormNode {

    Node getLabel();

    Node getNode();

    void verify() throws InvalidValueException;

    void commit();

    void rollback();

}

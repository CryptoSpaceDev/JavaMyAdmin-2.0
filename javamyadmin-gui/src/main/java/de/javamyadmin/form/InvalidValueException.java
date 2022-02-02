package de.javamyadmin.form;

public class InvalidValueException extends Exception {

    private final FormNode node;

    public InvalidValueException(FormNode node, String message) {
        this(node, message, null);
    }

    public InvalidValueException(FormNode node, String message, Exception cause) {
        super(message, cause);
        this.node = node;
    }

    public FormNode getNode() {
        return node;
    }

}

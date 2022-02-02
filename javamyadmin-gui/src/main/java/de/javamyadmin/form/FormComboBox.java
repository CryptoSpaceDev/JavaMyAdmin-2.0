package de.javamyadmin.form;

import de.javamyadmin.config.ConfigurationParameter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javax.annotation.Nonnull;

public class FormComboBox<E extends Enum<E>> implements FormNode {

    private final ConfigurationParameter<E> parameter;
    private final Label label = new Label();
    private final ComboBox<E> comboBox = new ComboBox<>();

    public FormComboBox(@Nonnull ConfigurationParameter<E> parameter, List<E> items) {
        this(parameter, items, parameter.getKey());
    }

    public FormComboBox(@Nonnull ConfigurationParameter<E> parameter, List<E> items, String label) {
        this(parameter, items, label, null);
    }

    public FormComboBox(@Nonnull ConfigurationParameter<E> parameter, List<E> items, String message, Node graphic) {
        this.parameter = Objects.requireNonNull(parameter);
        label.setText(message);
        label.setGraphic(graphic);
        comboBox.getItems().addAll(items == null ? Collections.emptyList() : items);
    }

    @Override
    public Label getLabel() {
        return label;
    }

    @Override
    public ComboBox<E> getNode() {
        return comboBox;
    }

    @Override
    public void verify() {
        // always valid
    }

    @Override
    public void commit() {
        parameter.setValue(comboBox.getValue());
    }

    @Override
    public void rollback() {
        comboBox.setValue(parameter.getValueOrDefault());
    }

}

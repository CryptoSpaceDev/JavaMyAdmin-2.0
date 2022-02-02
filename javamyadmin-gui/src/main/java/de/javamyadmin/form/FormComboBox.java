package de.javamyadmin.form;

import de.javamyadmin.config.ConfigurationParameter;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javax.annotation.Nonnull;

public class FormComboBox<E extends Enum<E>> implements FormNode {

    private final ConfigurationParameter<E> parameter;
    private final Label label = new Label();
    private final ComboBox<E> comboBox = new ComboBox<>();

    public FormComboBox(ConfigurationParameter<E> parameter, @Nonnull Class<E> enumClass) {
        this(parameter, enumClass, parameter == null ? "" : parameter.getKey());
    }

    public FormComboBox(ConfigurationParameter<E> parameter, @Nonnull Class<E> enumClass, String label) {
        this(parameter, enumClass, label, null);
    }

    public FormComboBox(ConfigurationParameter<E> parameter, @Nonnull Class<E> enumClass, String message, Node graphic) {
        this.parameter = parameter;
        label.setText(message);
        label.setGraphic(graphic);
        comboBox.itemsProperty().addListener(change -> selectFirstItemIfPossible());
        comboBox.getItems().addAll(enumClass.getEnumConstants());
        comboBox.setMaxWidth(Double.MAX_VALUE);

        if (parameter != null) {
            comboBox.getSelectionModel().select(parameter.getValue().orElse(null));
        }

        selectFirstItemIfPossible();
    }

    private void selectFirstItemIfPossible() {
        if (comboBox.getItems().size() == 1 && comboBox.getSelectionModel().getSelectedItem() == null) {
            comboBox.getSelectionModel().selectFirst();
        }
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
        if (parameter != null) {
            parameter.setValue(comboBox.getValue());
        }
    }

    @Override
    public void rollback() {
        if (parameter != null) {
            comboBox.setValue(parameter.getValueOrDefault());
        }
    }

}

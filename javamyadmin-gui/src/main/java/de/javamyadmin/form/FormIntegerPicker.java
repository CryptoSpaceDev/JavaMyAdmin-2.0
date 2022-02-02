package de.javamyadmin.form;

import de.javamyadmin.config.ConfigurationParameter;
import de.javamyadmin.utils.ComponentUtils;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;

public class FormIntegerPicker implements FormNode {

    private final ConfigurationParameter<Integer> parameter;
    private final Label label = new Label();
    private final Spinner<Integer> spinner;

    public FormIntegerPicker(ConfigurationParameter<Integer> parameter, int min, int max, int step) {
        this(parameter, min, max, step, parameter == null ? "" : parameter.getKey());
    }

    public FormIntegerPicker(ConfigurationParameter<Integer> parameter, int min, int max, int step, String label) {
        this(parameter, min, max, step, label, null);
    }

    public FormIntegerPicker(ConfigurationParameter<Integer> parameter, int min, int max, int step, String message, Node graphic) {
        this.parameter = parameter;
        label.setText(message);
        label.setGraphic(graphic);
        spinner = new Spinner<>(min, max, step);
        spinner.setMaxWidth(Double.MAX_VALUE);

        ComponentUtils.addSpinnerTextValidator(spinner, min, max);

        if (parameter != null) {
            spinner.getValueFactory().setValue(parameter.getValueOrDefault());
        }
    }

    @Override
    public Label getLabel() {
        return label;
    }

    @Override
    public Spinner<Integer> getNode() {
        return spinner;
    }

    @Override
    public void verify() {
        // always valid
    }

    @Override
    public void commit() {
        if (parameter != null) {
            parameter.setValue(spinner.getValue());
        }
    }

    @Override
    public void rollback() {
        if (parameter != null) {
            spinner.getValueFactory().setValue(parameter.getValueOrDefault());
        }
    }

}

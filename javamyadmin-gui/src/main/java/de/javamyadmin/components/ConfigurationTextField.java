package de.javamyadmin.components;

import de.javamyadmin.config.ConfigurationParameter;
import java.util.Objects;
import javafx.scene.control.TextField;

public class ConfigurationTextField extends TextField {

    private final ConfigurationParameter<String> parameter;

    public ConfigurationTextField(ConfigurationParameter<String> parameter) {
        this.parameter = Objects.requireNonNull(parameter);
        setText(parameter.getValueOrDefault());
    }

    public void commit() {
        parameter.setValue(getText());
    }

}

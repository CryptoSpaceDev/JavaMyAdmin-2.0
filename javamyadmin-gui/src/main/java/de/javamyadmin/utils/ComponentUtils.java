package de.javamyadmin.utils;

import java.util.regex.Pattern;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class ComponentUtils {

    private static final Pattern hostValidator = Pattern.compile("^[a-zA-Z0-9.]*$");

    private ComponentUtils() {
    }

    public static void addTextFieldHostValidator(TextField textField) {
        textField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!hostValidator.matcher(newValue).matches()) {
                textField.setText(oldValue);
            }
        });
    }

    public static void addSpinnerTextValidator(Spinner<Integer> spinner, int min, int max) {
        spinner.setEditable(true);
        spinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                int val = Integer.parseInt(newValue);

                if (val < min || val > max) {
                    spinner.getEditor().setText(oldValue);
                }
            } catch (NumberFormatException e) {
                spinner.getEditor().setText(oldValue);
            }
        });
    }

}

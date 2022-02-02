package de.javamyadmin.utils;

import java.util.function.Function;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;

public class BindingUtils {

    public static StringBinding concatStrings(ObservableStringValue... values) {
        return concatStrings(false, values);
    }

    public static StringBinding concatStrings(boolean emptyOnNull, ObservableStringValue... values) {
        return Bindings.createStringBinding(() -> {
            StringBuilder stringBuilder = new StringBuilder();

            for (ObservableStringValue value : values) {
                stringBuilder.append(value.getValue() == null && emptyOnNull ? "" : value.getValue());
            }

            return stringBuilder.toString();
        }, values);
    }

    public static <T> StringBinding mapString(ObservableValue<T> value, Function<T, String> mapping) {
        return Bindings.createStringBinding(() -> mapping.apply(value.getValue()), value);
    }

}

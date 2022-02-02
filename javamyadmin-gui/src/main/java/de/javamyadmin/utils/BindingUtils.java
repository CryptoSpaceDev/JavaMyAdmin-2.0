package de.javamyadmin.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javax.annotation.Nonnull;

public class BindingUtils {

    public static StringBinding concatStrings(ObservableStringValue... values) {
        return concatStrings(false, values);
    }

    public static StringBinding concatStrings(boolean emptyOnAnyNull, ObservableStringValue... values) {
        return Bindings.createStringBinding(() -> {
            StringBuilder stringBuilder = new StringBuilder();

            for (ObservableStringValue value : values) {
                if (value.getValue() == null && emptyOnAnyNull) {
                    return "";
                } else {
                    stringBuilder.append(value.getValue() == null ? "" : value.getValue());
                }
            }

            return stringBuilder.toString();
        }, values);
    }

    public static <T> StringBinding mapString(@Nonnull ObservableValue<T> value, @Nonnull Function<T, String> mapping) {
        return Bindings.createStringBinding(() -> mapping.apply(value.getValue()), value);
    }

    public static StringBinding urlSafeStringBinding(@Nonnull ObservableStringValue value) {
        return mapString(value, val -> val == null ? "" : URLEncoder.encode(val, StandardCharsets.UTF_8));
    }

}

package de.javamyadmin.config;

import java.nio.file.Path;
import javax.annotation.Nullable;

@FunctionalInterface
public interface Serializer<T> {

    String serialize(T value) throws CouldNotSerializeValueException;

    static String stringSerializer(@Nullable String value) {
        return value == null ? "" : value;
    }

    static String intSerializer(@Nullable Integer value) {
        return value == null ? "" : String.valueOf(value);
    }

    static <E extends Enum<E>> String enumSerializer(@Nullable E value) {
        return value == null ? "" : value.name();
    }

    static String pathSerializer(@Nullable Path value) {
        return value == null ? "" : String.valueOf(value);
    }

}

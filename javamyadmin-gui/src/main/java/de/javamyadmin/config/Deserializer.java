package de.javamyadmin.config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@FunctionalInterface
public interface Deserializer<T> {

    T deserialize(String value) throws CouldNotDeserializeValueException;

    @Nullable
    static String stringDeserializer(String value) {
        return "null".equals(value) || "".equals(value) ? null : value;
    }

    @Nullable
    static Integer intDeserializer(String value) throws CouldNotDeserializeValueException {
        if (value == null) {
            return null;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new CouldNotDeserializeValueException("%s is not a valid integer".formatted(value));
        }
    }

    @Nullable
    static <E extends Enum<E>> E enumDeserializer(@Nonnull Class<E> enumClass, String value) throws CouldNotDeserializeValueException {
        if (value == null) {
            return null;
        }

        try {
            return Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException e) {
            throw new CouldNotDeserializeValueException("%s is not a valid option".formatted(value));
        }
    }

}

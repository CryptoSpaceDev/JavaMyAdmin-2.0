package de.javamyadmin.config;

@FunctionalInterface
public interface Deserializer<T> {

    T deserialize(String value) throws CouldNotDeserializeValueException;

    static String stringDeserializer(String value) {
        return "null".equals(value) || "".equals(value) ? null : value;
    }

    static Integer intDeserializer(String value) throws CouldNotDeserializeValueException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new CouldNotDeserializeValueException(String.format("%s is not a valid integer", value));
        }
    }

}

package de.javamyadmin.config;

@FunctionalInterface
public interface Serializer<T> {

    String serialize(T value) throws CouldNotSerializeValueException;


    static String stringSerializer(String value) {
        return value == null ? "" : value;
    }

    static String intSerializer(Integer value) {
        return value == null ? "" : String.valueOf(value);
    }

    static <E extends Enum<E>> String enumSerializer(E value) {
        return value == null ? "" : value.name();
    }

}

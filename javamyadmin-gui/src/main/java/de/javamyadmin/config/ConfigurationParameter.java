package de.javamyadmin.config;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ConfigurationParameter<T> {

    private static final Map<String, ConfigurationParameter<?>> parameters = new LinkedHashMap<>();

    private final String key;
    private final T defaultValue;
    private final String comment;
    private final Deserializer<T> deserializer;
    private final Serializer<T> serializer;
    private final Property<T> valueProperty = new SimpleObjectProperty<>();

    private ConfigurationParameter(String key, @Nullable T defaultValue, @Nullable String comment, Deserializer<T> deserializer,
        Serializer<T> serializer) {
        this.defaultValue = defaultValue;
        this.key = key;
        this.comment = comment;
        this.deserializer = deserializer;
        this.serializer = serializer;
    }

    public String getKey() {
        return key;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public T getValueOrDefault() {
        return getValue().orElse(defaultValue);
    }

    public Property<T> getValueProperty() {
        return valueProperty;
    }

    public Optional<T> getValue() {
        return Optional.ofNullable(valueProperty.getValue());
    }

    public void setValue(T value) {
        valueProperty.setValue(value);
    }

    public Optional<String> getComment() {
        return Optional.ofNullable(comment);
    }

    public Deserializer<T> getDeserializer() {
        return deserializer;
    }

    public Serializer<T> getSerializer() {
        return serializer;
    }

    public static void iterateParameters(Consumer<ConfigurationParameter<?>> parameterConsumer) {
        parameters.forEach((key, parameter) -> parameterConsumer.accept(parameter));
    }

    public static void loadParameterValue(String key, String value) throws CouldNotDeserializeValueException {
        ConfigurationParameter<?> parameter = parameters.get(key);

        if (parameter == null) {
            throw new CouldNotDeserializeValueException("Unknown key: %s".formatted(key));
        } else {
            loadParameterValue(parameter, value);
        }
    }

    private static <T> void loadParameterValue(ConfigurationParameter<T> parameter, String value) throws CouldNotDeserializeValueException {
        Deserializer<T> deserializer = parameter.getDeserializer();
        T val = deserializer.deserialize(value);
        parameter.setValue(val);
    }

    public static ConfigurationParameter<String> registerStringParameter(String key, String defaultValue) {
        ConfigurationParameter<String> parameter = new ConfigurationParameter<>(
            key,
            defaultValue,
            "String",
            Deserializer::stringDeserializer,
            Serializer::stringSerializer
        );

        parameters.put(key, parameter);
        return parameter;
    }

    public static ConfigurationParameter<Integer> registerIntParameter(String key, Integer defaultValue) {
        ConfigurationParameter<Integer> parameter = new ConfigurationParameter<>(
            key,
            defaultValue,
            "Integer",
            Deserializer::intDeserializer,
            Serializer::intSerializer
        );

        parameters.put(key, parameter);
        return parameter;
    }

    public static <E extends Enum<E>> ConfigurationParameter<E> registerEnumParameter(String key, E defaultValue, @Nonnull Class<E> enumClass) {
        ConfigurationParameter<E> parameter = new ConfigurationParameter<>(
            key,
            defaultValue,
            "Possible values: %s".formatted(Arrays.stream(enumClass.getEnumConstants()).map(E::name).collect(Collectors.joining(", "))),
            value -> Deserializer.enumDeserializer(enumClass, value),
            Serializer::enumSerializer
        );

        parameters.put(key, parameter);
        return parameter;
    }

    public static ConfigurationParameter<Path> registerPathParameter(String key, Path defaultValue) {
        ConfigurationParameter<Path> parameter = new ConfigurationParameter<>(
            key,
            defaultValue,
            "Path",
            Deserializer::pathDeserializer,
            Serializer::pathSerializer
        );

        parameters.put(key, parameter);
        return parameter;
    }

}

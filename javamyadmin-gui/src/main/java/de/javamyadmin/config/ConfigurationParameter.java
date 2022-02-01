package de.javamyadmin.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConfigurationParameter<T> {

    private static final Map<String, ConfigurationParameter<?>> parameters = new HashMap<>();

    private final String key;
    private final T defaultValue;
    private final Deserializer<T> deserializer;
    private final Serializer<T> serializer;
    private T value;

    public ConfigurationParameter(String key, T defaultValue, Deserializer<T> deserializer, Serializer<T> serializer) {
        this.defaultValue = defaultValue;
        this.key = key;
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

    public Optional<T> getValue() {
        return Optional.ofNullable(value);
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Deserializer<T> getDeserializer() {
        return deserializer;
    }

    public Serializer<T> getSerializer() {
        return serializer;
    }

    public static Map<String, ConfigurationParameter<?>> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    public static ConfigurationParameter<String> registerStringParameter(String key, String defaultValue) {
        ConfigurationParameter<String> parameter = new ConfigurationParameter<>(
            key,
            defaultValue,
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
            Deserializer::intDeserializer,
            Serializer::intSerializer
        );

        parameters.put(key, parameter);
        return parameter;
    }

}
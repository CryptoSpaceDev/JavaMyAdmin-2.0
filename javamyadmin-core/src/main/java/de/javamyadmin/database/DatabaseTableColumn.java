package de.javamyadmin.database;

public record DatabaseTableColumn<T>(String name, Class<T> valueClass) {
}

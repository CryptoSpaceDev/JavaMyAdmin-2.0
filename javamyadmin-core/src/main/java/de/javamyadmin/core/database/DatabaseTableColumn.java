package de.javamyadmin.core.database;

public record DatabaseTableColumn<T>(String name, Class<T> valueClass) {

}

package de.javamyadmin.config;

import de.javamyadmin.form.InvalidValueException;

@FunctionalInterface
public interface FormVerifier {

    void verify() throws InvalidValueException;

}

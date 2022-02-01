package de.javamyadmin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ErrorReporter {

    void reportError(int lineNumber, String message);

    class Print implements ErrorReporter {

        private static final Logger log = LoggerFactory.getLogger(ErrorReporter.Print.class);


        @Override
        public void reportError(int lineNumber, String message) {
            log.warn("{}: {}", lineNumber, message);
        }

    }

}

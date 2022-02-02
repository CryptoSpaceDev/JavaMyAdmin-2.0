package de.javamyadmin.config;

import static de.javamyadmin.config.ConfigurationParameter.registerEnumParameter;
import static de.javamyadmin.config.ConfigurationParameter.registerIntParameter;
import static de.javamyadmin.config.ConfigurationParameter.registerStringParameter;

import de.javamyadmin.core.database.DatabaseSystem;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {

    public static final ConfigurationParameter<DatabaseSystem> DATABASE_SYSTEM = registerEnumParameter("DATABASE_SYSTEM", null, DatabaseSystem.class);
    public static final ConfigurationParameter<String> DATABASE_HOST = registerStringParameter("DATABASE_HOST", null);
    public static final ConfigurationParameter<Integer> DATABASE_PORT = registerIntParameter("DATABASE_PORT", null);
    public static final ConfigurationParameter<String> DATABASE_NAME = registerStringParameter("DATABASE_NAME", null);
    public static final ConfigurationParameter<String> DATABASE_USER = registerStringParameter("DATABASE_USER", null);
    public static final ConfigurationParameter<String> DATABASE_PASS = registerStringParameter("DATABASE_PASSWORD", null);

    private static final Logger log = LoggerFactory.getLogger(Configuration.class);
    private static final Pattern keyValueRegex = Pattern.compile("^([^=]+)=(.*)$");

    private Configuration() {
    }

    public static void loadFrom(InputStream inputStream, ErrorReporter reporter) throws IOException {
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            String lineTrimmed = line.trim();

            if (lineTrimmed.startsWith("#") || lineTrimmed.isEmpty()) {
                continue;
            }

            parseLine(reader.getLineNumber(), lineTrimmed, reporter);
        }
    }

    private static void parseLine(int lineNumber, String line, ErrorReporter reporter) {
        Matcher matcher = keyValueRegex.matcher(line);

        if (matcher.matches()) {
            String key = matcher.group(1);
            String value = matcher.group(2);

            try {
                ConfigurationParameter.loadParameterValue(key, value);
            } catch (CouldNotDeserializeValueException e) {
                log.error(e.getMessage(), e);
                reporter.reportError(lineNumber, e.getMessage());
            }
        } else {
            reporter.reportError(lineNumber, "Invalid line, must contain character '='");
        }
    }

    public static void saveTo(OutputStream outputStream, ErrorReporter reporter) {
        PrintStream printStream = new PrintStream(outputStream);
        AtomicInteger index = new AtomicInteger(1);

        printStream.println("# Do not edit this file while the application is running!");
        printStream.println();
        ConfigurationParameter.iterateParameters(parameter -> writeLine(parameter, index.getAndIncrement(), printStream, reporter));
    }

    private static <T> void writeLine(ConfigurationParameter<T> parameter, int lineNumber, PrintStream printStream, ErrorReporter reporter) {
        Serializer<T> serializer = parameter.getSerializer();

        try {
            String value = serializer.serialize(parameter.getValue().orElse(parameter.getDefaultValue()));

            printStream.print(parameter.getKey());
            printStream.print('=');
            printStream.println(value);
        } catch (CouldNotSerializeValueException e) {
            log.error(e.getMessage(), e);
            reporter.reportError(lineNumber, e.getMessage());
        }
    }

}

package battleships.util;

// Thrown when the user-provided configuration file is invalid
public class InvalidConfigException extends Exception {

    // Constructs the exception with a message
    public InvalidConfigException(String message) {
        super(message);
    }

    // Constructs the exception with a message and a cause
    public InvalidConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}

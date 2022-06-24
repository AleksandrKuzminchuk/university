package ua.foxminded.task10.uml.exceptions;

public class PropertiesException extends RuntimeException {
    public PropertiesException(Exception exception) {
        addSuppressed(exception);
    }
}

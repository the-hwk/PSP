package exceptions;

public class WrongMessageFormatException extends Exception {
    public WrongMessageFormatException(String message) {
        super(message);
    }

    public WrongMessageFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
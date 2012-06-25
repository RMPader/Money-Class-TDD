package currency.exceptions;

@SuppressWarnings(value = { "serial" })
public class InvalidMoneyValueException extends RuntimeException {
    public InvalidMoneyValueException(String message) {
	super("Invalid format for " + message);
    }
}

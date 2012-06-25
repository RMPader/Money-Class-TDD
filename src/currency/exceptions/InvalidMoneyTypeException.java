package currency.exceptions;

@SuppressWarnings(value = { "serial" })
public class InvalidMoneyTypeException extends RuntimeException {
    public InvalidMoneyTypeException(String message) {
	super(message);
    }
}

package currency.Exceptions;

@SuppressWarnings("serial")
public class IncompatibleCurrencyException extends RuntimeException {

    public IncompatibleCurrencyException(String message) {
	super(message);
    }

}

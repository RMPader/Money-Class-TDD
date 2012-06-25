package currency.exceptions;

@SuppressWarnings(value = { "serial" })
public class IncompatibleCurrencyException extends RuntimeException {

    public IncompatibleCurrencyException(String msg) {
	super(msg);
    }
}

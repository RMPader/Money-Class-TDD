package currency.exceptions;

@SuppressWarnings("serial")
public class DecimalInputOutOfRange extends RuntimeException {

    public DecimalInputOutOfRange(String message) {
	super(message);
    }
}

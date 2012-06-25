package currency;

public enum Currency {
    EUR("EUR"), USD("USD"), PHP("PHP");
    private Currency(String stringValue) {
	this.stringValue = stringValue;
    }

    @Override
    public String toString() {
	return stringValue;
    }

    private final String stringValue;
}

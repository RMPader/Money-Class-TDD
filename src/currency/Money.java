package currency;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import currency.exceptions.DecimalInputOutOfRange;
import currency.exceptions.IncompatibleCurrencyException;
import currency.exceptions.InvalidMoneyValueException;

public class Money {

    private final int decimalNumber;
    private final int wholeNumber;
    private final boolean valueIsNegativeFractional;
    private final Currency currency;

    public Money(Currency currency, String value) {
	this.currency = currency;
	this.wholeNumber = extractWholeNumber(value);
	int decimal = extractDecimalNumber(value);
	if(value.charAt(0)=='-'){
	    decimal *= -1;
	}
	this.decimalNumber = decimal;
	if (value.charAt(0) == '-' && wholeNumber == 0) {
	    valueIsNegativeFractional = true;
	} else {
	    valueIsNegativeFractional = false;
	}
    }

    private Money(Currency currency, int wholeNumber, int decimalNumber)
	    throws InvalidMoneyValueException {
	checkDecimalOutOfRange(decimalNumber);
	checkSimilarSign(wholeNumber, decimalNumber);
	if (decimalNumber < 0 && wholeNumber == 0) {
	    valueIsNegativeFractional = true;
	} else {
	    valueIsNegativeFractional = false;
	}
	this.currency = currency;
	this.wholeNumber = wholeNumber;
	this.decimalNumber = decimalNumber;
    }

    private static final int MONEY_VALUE_WHOLE_NUMBER_INDEX = 0;
    private static final int MONEY_VALUE_DECIMAL_NUMBER_INDEX = 1;
    private static final int DECIMAL_PRECISION = 2;

    private static final int MONEY_CURRENCY_INDEX = 0;
    private static final int MONEY_VALUE_INDEX = 1;

    private static StringBuilder createMoneyTypeExceptionMessage(
	    String suspectString) {
	StringBuilder errorMessage = new StringBuilder(suspectString);
	errorMessage.append(", type can only be ");
	return appendCurrencyTypes(errorMessage);
    }

    private static StringBuilder appendCurrencyTypes(StringBuilder errorMessage) {
	for (Currency type : Currency.values()) {
	    errorMessage.append(type.toString());
	    errorMessage.append(",");
	}
	return errorMessage.append(".");
    }

    private static int extractWholeNumber(String valuePart) {
	String splitValue[] = valuePart.split("\\.");
	if (splitValue.length > 2) {
	    throw new InvalidMoneyValueException(valuePart
		    + ": input has many decimal points");
	}
	return Integer.parseInt(splitValue[MONEY_VALUE_WHOLE_NUMBER_INDEX]);
    }

    private static int extractDecimalNumber(String valuePart) {
	String decimalFromInput = extractDecimalFromInput(valuePart);
	if (decimalPrecisionIsMoreThanTwo(decimalFromInput)) {
	    throw new InvalidMoneyValueException(valuePart
		    + " has higher precision. Expected is 2 (e.g 1.00, 30.01)");
	}
	return Integer.parseInt(decimalFromInput);
    }

    private static String extractDecimalFromInput(String value) {
	String[] splitValue = value.split("\\.");
	String decimalFromInput = splitValue[MONEY_VALUE_DECIMAL_NUMBER_INDEX];
	if (decimalFromInput.length() == 1) {
	    decimalFromInput = decimalFromInput + "0";
	}
	return decimalFromInput;
    }

    private static boolean decimalPrecisionIsMoreThanTwo(String decimalNumber) {
	return decimalNumber.length() > DECIMAL_PRECISION;
    }

    private void checkDecimalOutOfRange(int decimalNumber) {
	if (decimalNumber >= 100) {
	    StringBuilder message = concatAll("invalid decimal: ",
		    String.valueOf(decimalNumber),
		    " must be within range of 1 to 99");
	    throw new DecimalInputOutOfRange(message.toString());
	}
    }

    private void checkSimilarSign(int whole, int decimal) {
	long sign = wholeNumber * decimalNumber;
	if (sign < 0) {
	    throw new InvalidMoneyValueException(
		    " both arguments have non-zero values that are different in sign.");
	}
    }

    public Money multiply(double multiplicand) {
	double productWholeNumber = ((double) wholeNumber) * multiplicand;
	double productDecimal = ((double) decimalNumber) * multiplicand;
	return createMoney(currency, productWholeNumber, productDecimal);
    }

    public Money divide(double dividend) {
	if (dividend == 0) {
	    throw new ArithmeticException("Division by zero.");
	}
	double productWholeNumber = ((double) wholeNumber) / dividend;
	double productDecimal = ((double) decimalNumber) / dividend;
	return createMoney(currency, productWholeNumber, productDecimal);

    }

    private static Money createMoney(Currency currency,
	    double productWholeNumber, double productDecimal) {
	double result = createDoubleForm(productWholeNumber, productDecimal);
	DecimalFormat doubleRep = new DecimalFormat("0.00");
	doubleRep.setRoundingMode(RoundingMode.HALF_UP);
	String sb = doubleRep.format(result);
	int whole = Integer.parseInt(sb.substring(0, sb.indexOf(".")));
	int decimal = Integer.parseInt(sb.substring(sb.indexOf(".") + 1,
		sb.length()));
	if (whole == 0 && result < 0) {
	    decimal *= -1;
	}
	return new Money(currency, whole, decimal);
    }

    private static double createDoubleForm(double productWholeNumber,
	    double productDecimal) {
	return productWholeNumber + (productDecimal / 100);
    }

    public Money add(Money addend) {
	checkisSameCurrency(addend);
	int wholeNumber = this.wholeNumber + addend.wholeNumber;
	int decimalNumber = this.decimalNumber + addend.decimalNumber;
	if (decimalNumber >= 100) {
	    wholeNumber = wholeNumber + (decimalNumber / 100);
	    decimalNumber = decimalNumber % 100;
	}
	return new Money(currency, wholeNumber, decimalNumber);
    }

    public void checkisSameCurrency(Money money) {
	if (currency != money.currency) {
	    StringBuilder sb = concatAll("cannot perform operation on ",
		    currency.toString(), " and ", money.currency.toString());
	    String message = sb.toString();
	    throw new IncompatibleCurrencyException(message);
	}
    }

    public Money subtract(Money subtrahend) {
	checkisSameCurrency(subtrahend);
	int wholeNumber = this.wholeNumber - subtrahend.wholeNumber;
	int decimalNumber = this.decimalNumber - subtrahend.decimalNumber;
	decimalNumber *= -1;
	System.out.println(wholeNumber + "." + decimalNumber);
	return new Money(currency, wholeNumber, decimalNumber);
    }

    private static StringBuilder concatAll(String... strings) {
	StringBuilder newString = new StringBuilder();
	for (String s : strings) {
	    newString.append(s);
	}
	return newString;
    }

    public String getCurrencyType() {
	return currency.toString();
    }

    public String getValue() {
	StringBuilder value;
	if (valueIsNegativeFractional) {
	    value = concatAll("-", Integer.toString(wholeNumber), ".",
		    Integer.toString(Math.abs(decimalNumber)));
	} else {
	    value = concatAll(Integer.toString(wholeNumber), ".",
		    Integer.toString(Math.abs(decimalNumber)));
	}
	if (Math.abs(decimalNumber) < 10) {
	    value.insert(value.indexOf(".") + 1, '0');
	}
	return value.toString();
    }

    @Override
    public String toString() {
	StringBuilder sb = concatAll(getCurrencyType(), " ", getValue());
	return sb.toString();
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((currency == null) ? 0 : currency.hashCode());
	result = prime * result + decimalNumber;
	result = prime * result + (valueIsNegativeFractional ? 1231 : 1237);
	result = prime * result + wholeNumber;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (!(obj instanceof Money)) {
	    return false;
	}
	Money other = (Money) obj;
	if (currency != other.currency) {
	    return false;
	}
	if (decimalNumber != other.decimalNumber) {
	    return false;
	}
	if (valueIsNegativeFractional != other.valueIsNegativeFractional) {
	    return false;
	}
	if (wholeNumber != other.wholeNumber) {
	    return false;
	}
	return true;
    }
}

package currency;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import currency.exceptions.IncompatibleCurrencyException;
import currency.exceptions.InvalidMoneyValueException;

public class Money {
    private static final int MONEY_VALUE_WHOLE_NUMBER_INDEX = 0;
    private static final int MONEY_VALUE_DECIMAL_NUMBER_INDEX = 1;
    private static final int DECIMAL_PRECISION = 2;

    private final int decimalNumber;
    private final int wholeNumber;
    private final Currency currency;
    private final boolean valueIsNegativeFractional;

    public Money(Currency currency, String value) {
	this.currency = currency;
	this.wholeNumber = extractWholeNumber(value);
	this.decimalNumber = extractDecimalNumber(value);
	this.valueIsNegativeFractional = 
		(value.charAt(0) == '-' && wholeNumber == 0) ? true: false;
    }

    private Money(Currency currency, int wholeNumber, int decimalNumber) {
	this.currency = currency;
	this.wholeNumber = wholeNumber;
	this.decimalNumber = decimalNumber;
	this.valueIsNegativeFractional = 
		(decimalNumber < 0 && wholeNumber == 0) ? true: false;
    }

    private static int extractWholeNumber(String valuePart) {
	String splitValue[] = valuePart.split("\\.");
	return Integer.parseInt(splitValue[MONEY_VALUE_WHOLE_NUMBER_INDEX]);
    }

    private static int extractDecimalNumber(String valuePart) {
	String decimalFromInput = extractDecimalFromInput(valuePart);
	if (decimalPrecisionIsMoreThanTwo(decimalFromInput)) {
	    throw new InvalidMoneyValueException(valuePart
		    + " has higher precision. Expected is 2 (e.g 1.00, 30.01)");
	}
	int decimal = Integer.parseInt(decimalFromInput);
	if (valuePart.charAt(0) == '-') {
	    decimal *= -1;
	}
	return decimal;
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
	if (mustBorrow(subtrahend.decimalNumber))
	    if (decimalNumber < 0) {
		int borrow = 100;
		wholeNumber--;
		decimalNumber = Math.abs(decimalNumber + borrow);
	    }
	if (wholeNumber > 0 && decimalNumber < 0) {
	    decimalNumber *= -1;
	}
	return new Money(currency, wholeNumber, decimalNumber);
    }

    private boolean mustBorrow(int decimalNumber) {
	return this.wholeNumber > 0
		|| (this.wholeNumber < 0 && decimalNumber < 0);
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

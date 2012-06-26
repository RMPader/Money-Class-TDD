package currency;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import currency.exceptions.IncompatibleCurrencyException;
import currency.exceptions.InvalidMoneyValueException;

public class Money {

    private final int decimalNumber;
    private final int wholeNumber;
    private final boolean valueIsNegativeFractional;
    private final Currency currency;

    public Money(Currency currency, int wholeNumber, int decimalNumber)
	    throws InvalidMoneyValueException {
	long sign = wholeNumber * decimalNumber;
	if (sign < 0) {
	    throw new InvalidMoneyValueException(
		    " both arguments have non-zero values that are different in sign.");
	} else {
	    if (decimalNumber < 0) {
		valueIsNegativeFractional = true;
	    } else {
		valueIsNegativeFractional = false;
	    }
	    this.currency = currency;
	    this.wholeNumber = wholeNumber;
	    this.decimalNumber = Math.abs(decimalNumber);
	}
    }

    public Money multiply(double multiplicand) {
	double productWholeNumber = ((double) wholeNumber) * multiplicand;
	double productDecimal = ((double) decimalNumber) * multiplicand;
	return createMoney(currency, productWholeNumber, productDecimal);
    }

    public Money divide(float dividend) {
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
	    wholeNumber++;
	    decimalNumber %= 100;
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
	if (decimalNumber < 0) {
	    int borrow = 100;
	    if (this.decimalNumber < 10)
		borrow = 10;
	    wholeNumber--;
	    decimalNumber = Math.abs(decimalNumber + borrow);
	}
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
	String sign = "";
	if (valueIsNegativeFractional) {
	    sign = "-";
	}
	StringBuilder value = concatAll(sign, Integer.toString(wholeNumber),
		".", Integer.toString(decimalNumber));
	if (decimalNumber < 10) {
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

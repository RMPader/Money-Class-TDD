package currency;

import java.math.BigDecimal;

import currency.exceptions.IncompatibleCurrencyException;
import currency.exceptions.InvalidMoneyValueException;

public class Money {

    private final int decimalNumber;
    private final int wholeNumber;
    private final boolean hasNegativeValue;
    private final Currency currency;

    public Money(Currency currency, int wholeNumber, int decimalNumber) throws InvalidMoneyValueException {
	if (wholeNumber == 0) {
	    hasNegativeValue = (decimalNumber < 0) ? true : false;
	} else if (wholeNumber < 0) {
	    hasNegativeValue = true;
	} else {
	    hasNegativeValue = false;
	}
	this.currency = currency;
	this.wholeNumber = wholeNumber;
	this.decimalNumber = decimalNumber;
    }

    public Money multiply(double multiplicand) {
	double productWholeNumber = ((double) wholeNumber) * multiplicand;
	double productDecimal = ((double) decimalNumber) * multiplicand;
	return abracadabra(currency, productWholeNumber, productDecimal);
    }

    public Money divide(float dividend) {
	double productWholeNumber = ((double) wholeNumber) / dividend;
	double productDecimal = ((double) decimalNumber) / dividend;
	return abracadabra(currency, productWholeNumber, productDecimal);
    }

    // TODO rename this muthafuka
    private static Money abracadabra(Currency currency, double productWholeNumber, double productDecimal) {
	double product = productWholeNumber + (productDecimal / 100);
	StringBuilder sb = new StringBuilder(Double.toString(product));
	int index = sb.indexOf(".");
	index = index + 3;
	if (Integer.parseInt(sb.substring(index, index + 1)) >= 5) {
	    sb.setCharAt(index - 1, Character.forDigit(Integer.parseInt(sb.substring(index - 1, index)) + 1, 10));
	}
	sb.delete(index, sb.length());
	int whole = Integer.parseInt(sb.substring(0, sb.indexOf(".")));
	int decimal = Integer.parseInt(sb.substring(sb.indexOf(".") + 1, sb.length()));
	return new Money(currency, whole, decimal);
    }

    // ////
    // /PUT YOUR ADD/SUBTRACT STUFF HERE
    // ////
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
	    String message = concatAll("cannot perform operation on ", currency.toString(), " and ", money.currency.toString());
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

    private static String concatAll(String... strings) {
	StringBuilder newString = new StringBuilder();
	for (String s : strings) {
	    newString.append(s);
	}
	return newString.toString();
    }

    public String getCurrencyType() {
	return currency.toString();
    }

    public BigDecimal getValue() {
	String valueString = concatAll(Integer.toString(wholeNumber), ".", Integer.toString(decimalNumber));
	BigDecimal bd = new BigDecimal(valueString);
	return bd;
    }

    @Override
    public String toString() {
	String toReturn = concatAll(currency.toString(), " ", Integer.toString(wholeNumber), ".", Integer.toString(decimalNumber));
	return toReturn;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((currency == null) ? 0 : currency.hashCode());
	result = prime * result + decimalNumber;
	result = prime * result + (hasNegativeValue ? 1231 : 1237);
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
	if (hasNegativeValue != other.hasNegativeValue) {
	    return false;
	}
	if (wholeNumber != other.wholeNumber) {
	    return false;
	}
	return true;
    }
}

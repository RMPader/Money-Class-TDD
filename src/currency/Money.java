package currency;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import currency.exceptions.InvalidMoneyValueException;

public class Money {

    private final int decimalNumber;
    private final int wholeNumber;
    private final boolean hasNegativeValue;
    private final Currency currency;

    public Money(Currency currency, int wholeNumber, int decimalNumber)
	    throws InvalidMoneyValueException {
	long sign = wholeNumber * decimalNumber;
	if(sign < 0){
	    throw new InvalidMoneyValueException(" both arguments have non-zero values that are different in sign.");
	} else {
	if(wholeNumber < 0 || decimalNumber < 0){
	    hasNegativeValue = true;
	} else { 
	    hasNegativeValue = false;
	}
	this.currency = currency;
	this.wholeNumber = wholeNumber;
	this.decimalNumber = decimalNumber;
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
	System.out.println(sb);
	int whole = Integer.parseInt(sb.substring(0, sb.indexOf(".")));
	int decimal = Integer.parseInt(sb.substring(sb.indexOf(".") + 1,
		sb.length()));
	return new Money(currency, whole, decimal);
    }

    private static double createDoubleForm(double productWholeNumber,
	    double productDecimal) {
	return productWholeNumber + (productDecimal / 100);
    }

    //ADD-SUBTRACT
    
    
    
    
    
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
	StringBuilder value = concatAll(Integer.toString(wholeNumber), ".",
		Integer.toString(decimalNumber));
	if (decimalNumber < 10) {
	    value.insert(value.indexOf(".") + 1, '0');
	}
	if(hasNegativeValue){
	    value.insert(0, '-');
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

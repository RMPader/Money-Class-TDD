package currency.tests;

import static org.junit.
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Test;

import currency.Money;
import currency.exceptions.IncompatibleCurrencyException;

public class MoneyOperationTest {

    @Test
    public void incompatibleCurrencyUsingAddition() {
	Money augend = new Money("USD 1.0");
	Money addend = new Money("EUR 2.0");
	try {
	    augend.add(addend);
	} catch (IncompatibleCurrencyException e) {
	}
	augend = new Money("USD 1.0");
	addend = new Money("PHP 2.0");
	try {
	    augend.add(addend);
	} catch (IncompatibleCurrencyException e) {
	}
	augend = new Money("PHP 1.0");
	addend = new Money("EUR 2.0");
	try {
	    augend.add(addend);
	} catch (IncompatibleCurrencyException e) {
	}
    }

    @Test
    public void incompatibleCurrencyUsingSubtraction() {
	Money minuend = new Money("USD 1.0");
	Money subtrahend = new Money("EUR 2.0");
	try {
	    minuend.subtract(subtrahend);
	} catch (IncompatibleCurrencyException e) {
	}
	minuend = new Money("USD 1.0");
	subtrahend = new Money("PHP 2.0");
	try {
	    minuend.subtract(subtrahend);
	} catch (IncompatibleCurrencyException e) {
	}
	minuend = new Money("PHP 1.0");
	subtrahend = new Money("EUR 2.0");
	try {
	    minuend.subtract(subtrahend);
	} catch (IncompatibleCurrencyException e) {
	}
    }

    @Test
    public void divisionByZero() {
	try {
	    Money multiplier = new Money("PHP 0.1");
	    multiplier.divide("0");
	    fail("must throw ArithmeticException. Cannot divide by zero");
	} catch (ArithmeticException e) {
	}
    }

    @Test
    public void divisionWithRoundingOff() {
	Money dividend = new Money("USD 0.06");
	Money result = new Money("USD 0.01");
	Money expected = dividend.divide("5");
	assertEquals(expected, result);

	dividend = new Money("USD 0.02");
	result = new Money("USD 0");
	expected = dividend.divide("5");
	assertEquals(expected, result);

	dividend = new Money("USD 0.02");
	result = new Money("USD 0");
	expected = dividend.divide("5");
	assertEquals(expected, result);

	dividend = new Money("USD 0.05");
	result = new Money("USD .01");
	expected = dividend.divide("5");
	assertEquals(expected, result);
    }

    @Test
    public void multiplyWithRoundingOff() {
	Money multiplier = new Money("USD 0.01");
	Money result = new Money("USD .01");
	Money expected = multiplier.multiply(".5");
	assertEquals(expected, result);

	multiplier = new Money("USD 0.01");
	result = new Money("USD 0");
	expected = multiplier.multiply(".1");
	assertEquals(expected, result);

	multiplier = new Money("USD 1");
	result = new Money("USD 0");
	expected = multiplier.multiply(".001");
	assertEquals(expected, result);

	multiplier = new Money("USD 5");
	result = new Money("USD .01");
	expected = multiplier.multiply(".001");
	assertEquals(expected, result);
    }

    @Test
    public void positiveCurrencyDivision() {
	Money dividend = new Money("PHP 0.1");
	Money result = dividend.divide("2");
	Money expected = new Money("PHP 0.05");
	assertEquals(expected, result);
	assertEquals("PHP 0.05", result.toString());
	assertEquals(new BigDecimal("0.05"), result.getValue());

	dividend = new Money("USD 0");
	result = dividend.divide("0.1");
	expected = new Money("USD 0.0");
	assertEquals(expected, result);
	assertEquals("USD 0.00", result.toString());
	assertEquals(new BigDecimal("0.00"), result.getValue());

	dividend = new Money("PHP 0.1");
	result = dividend.divide("0.2");
	expected = new Money("PHP 0.5");
	assertEquals(expected, result);
	assertEquals("PHP 0.50", result.toString());
	assertEquals(new BigDecimal("0.50"), result.getValue());

	dividend = new Money("EUR 1.1");
	result = dividend.divide("0.2");
	expected = new Money("EUR 5.5");
	assertEquals(expected, result);
	assertEquals("EUR 5.50", result.toString());
	assertEquals(new BigDecimal("5.50"), result.getValue());

	dividend = new Money("PHP 5");
	result = dividend.divide("2");
	expected = new Money("PHP 2.5");
	assertEquals(expected, result);
	assertEquals("PHP 2.50", result.toString());
	assertEquals(new BigDecimal("2.50"), result.getValue());

	dividend = new Money("PHP 6");
	result = dividend.divide("2");
	expected = new Money("PHP 3");
	assertEquals(expected, result);
	assertEquals("PHP 3.00", result.toString());
	assertEquals(new BigDecimal("3.00"), result.getValue());
    }

    @Test
    public void negativeCurrencyDivision() {
	Money dividend = new Money("PHP -0.1");
	Money result = dividend.divide("2");
	Money expected = new Money("PHP -0.05");
	assertEquals(expected, result);
	assertEquals("PHP -0.05", result.toString());
	assertEquals(new BigDecimal("-0.05"), result.getValue());

	dividend = new Money("USD 0");
	result = dividend.divide("-0.1");
	expected = new Money("USD 0.0");
	assertEquals(expected, result);
	assertEquals("USD 0.00", result.toString());
	assertEquals(new BigDecimal("0.00"), result.getValue());

	dividend = new Money("PHP -0.1");
	result = dividend.divide("-0.2");
	expected = new Money("PHP 0.5");
	assertEquals(expected, result);
	assertEquals("PHP 0.50", result.toString());
	assertEquals(new BigDecimal("0.50"), result.getValue());

	dividend = new Money("EUR 1.1");
	result = dividend.divide("-0.2");
	expected = new Money("EUR -5.5");
	assertEquals(expected, result);
	assertEquals("EUR -5.50", result.toString());
	assertEquals(new BigDecimal("-5.50"), result.getValue());
    }

    @Test
    public void positiveCurrencyMultiplication() {
	Money multiplier = new Money("PHP 0.1");
	Money result = multiplier.multiply("2");
	Money expected = new Money("PHP 0.2");
	assertEquals(expected, result);
	assertEquals("PHP 0.20", result.toString());
	assertEquals(new BigDecimal("0.20"), result.getValue());

	multiplier = new Money("USD 0.1");
	result = multiplier.multiply("0");
	expected = new Money("USD 0.0");
	assertEquals(expected, result);
	assertEquals("USD 0.00", result.toString());
	assertEquals(new BigDecimal("0.00"), result.getValue());

	multiplier = new Money("PHP 0.1");
	result = multiplier.multiply("0.2");
	expected = new Money("PHP 0.02");
	assertEquals(expected, result);
	assertEquals("PHP 0.02", result.toString());
	assertEquals(new BigDecimal("0.02"), result.getValue());

	multiplier = new Money("EUR 1.1");
	result = multiplier.multiply("0.2");
	expected = new Money("EUR 0.22");
	assertEquals(expected, result);
	assertEquals("EUR 0.22", result.toString());
	assertEquals(new BigDecimal("0.22"), result.getValue());

	multiplier = new Money("PHP 5");
	result = multiplier.multiply("2");
	expected = new Money("PHP 10");
	assertEquals(expected, result);
	assertEquals("PHP 10.00", result.toString());
	assertEquals(new BigDecimal("10.00"), result.getValue());
    }

    @Test
    public void negativeCurrencyMultiplication() {
	Money multiplier = new Money("PHP -0.1");
	Money result = multiplier.multiply("-2");
	Money expected = new Money("PHP 0.2");
	assertEquals(expected, result);
	assertEquals("PHP 0.20", result.toString());
	assertEquals(new BigDecimal("0.20"), result.getValue());

	multiplier = new Money("USD -0.1");
	result = multiplier.multiply("0");
	expected = new Money("USD 0.0");
	assertEquals(expected, result);
	assertEquals("USD 0.00", result.toString());
	assertEquals(new BigDecimal("0.00"), result.getValue());

	multiplier = new Money("PHP -0.1");
	result = multiplier.multiply("0.2");
	expected = new Money("PHP -0.02");
	assertEquals(expected, result);
	assertEquals("PHP -0.02", result.toString());
	assertEquals(new BigDecimal("-0.02"), result.getValue());

	multiplier = new Money("EUR 1.1");
	result = multiplier.multiply("-0.2");
	expected = new Money("EUR -0.22");
	assertEquals(expected, result);
	assertEquals("EUR -0.22", result.toString());
	assertEquals(new BigDecimal("-0.22"), result.getValue());
    }

    @Test
    public void positiveCurrencyAddition() {
	Money augend = new Money("EUR -.1");
	Money addend = new Money("EUR -.2");
	Money result = augend.add(addend);
	Money expected = new Money("EUR -.3");
	assertEquals(expected, result);
	assertEquals("EUR -0.30", result.toString());
	assertEquals(new BigDecimal("-0.30"), result.getValue());

	augend = new Money("USD 0.51");
	addend = new Money("USD 0.51");
	result = augend.add(addend);
	expected = new Money("USD 1.02");
	assertEquals(expected, result);
	assertEquals("USD 1.02", result.toString());
	assertEquals(new BigDecimal("1.02"), result.getValue());

	augend = new Money("PHP 0.5");
	addend = new Money("PHP -.1");
	result = augend.add(addend);
	expected = new Money("PHP .4");
	assertEquals(expected, result);
	assertEquals("PHP 0.40", result.toString());
	assertEquals(new BigDecimal("0.40"), result.getValue());

	augend = new Money("PHP -.05");
	addend = new Money("PHP 0.10");
	result = augend.add(addend);
	expected = new Money("PHP .05");
	assertEquals(expected, result);
	assertEquals("PHP 0.05", result.toString());
	assertEquals(new BigDecimal("0.05"), result.getValue());

	augend = new Money("PHP .05");
	addend = new Money("PHP 0.10");
	result = augend.add(addend);
	expected = new Money("PHP .15");
	assertEquals(expected, result);
	assertEquals("PHP 0.15", result.toString());
	assertEquals(new BigDecimal("0.15"), result.getValue());

	augend = new Money("PHP 1.05");
	addend = new Money("PHP 2.10");
	result = augend.add(addend);
	expected = new Money("PHP 3.15");
	assertEquals(expected, result);
	assertEquals("PHP 3.15", result.toString());
	assertEquals(new BigDecimal("3.15"), result.getValue());

	augend = new Money("PHP 3");
	addend = new Money("PHP 2");
	result = augend.add(addend);
	expected = new Money("PHP 5");
	assertEquals(expected, result);
	assertEquals("PHP 5.00", result.toString());
	assertEquals(new BigDecimal("5.00"), result.getValue());
    }

    @Test
    public void positiveCurrencySubtraction() {
	Money augend = new Money("EUR .1");
	Money addend = new Money("EUR -.2");
	Money result = augend.subtract(addend);
	Money expected = new Money("EUR .3");
	assertEquals(expected, result);
	assertEquals("EUR 0.30", result.toString());
	assertEquals(new BigDecimal("0.30"), result.getValue());

	augend = new Money("USD 0.51");
	addend = new Money("USD 0.51");
	result = augend.subtract(addend);
	expected = new Money("USD 0");
	assertEquals(expected, result);
	assertEquals("USD 0.00", result.toString());
	assertEquals(new BigDecimal("0.00"), result.getValue());

	augend = new Money("PHP 0.5");
	addend = new Money("PHP .1");
	result = augend.subtract(addend);
	expected = new Money("PHP .4");
	assertEquals(expected, result);
	assertEquals("PHP 0.40", result.toString());
	assertEquals(new BigDecimal("0.40"), result.getValue());

	augend = new Money("EUR -.05");
	addend = new Money("EUR -0.10");
	result = augend.subtract(addend);
	expected = new Money("EUR .05");
	assertEquals(expected, result);
	assertEquals("EUR 0.05", result.toString());
	assertEquals(new BigDecimal("0.05"), result.getValue());

	augend = new Money("PHP -.05");
	addend = new Money("PHP 0.10");
	result = augend.subtract(addend);
	expected = new Money("PHP -.15");
	assertEquals(expected, result);
	assertEquals("PHP -0.15", result.toString());
	assertEquals(new BigDecimal("-0.15"), result.getValue());

	augend = new Money("PHP 1.05");
	addend = new Money("PHP 2.10");
	result = augend.subtract(addend);
	expected = new Money("PHP -1.05");
	assertEquals(expected, result);
	assertEquals("PHP -1.05", result.toString());
	assertEquals(new BigDecimal("-1.05"), result.getValue());

	augend = new Money("PHP 3");
	addend = new Money("PHP 2");
	result = augend.subtract(addend);
	expected = new Money("PHP 1");
	assertEquals(expected, result);
	assertEquals("PHP 1.00", result.toString());
	assertEquals(new BigDecimal("1.00"), result.getValue());
    }
}

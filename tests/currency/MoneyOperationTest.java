package currency;

import static org.junit.Assert.*;

import org.junit.Test;

import currency.Currency;
import currency.Money;
import currency.exceptions.IncompatibleCurrencyException;

public class MoneyOperationTest {

    @Test
    public void toStringTests() {
	Money php = new Money(Currency.PHP, "1.00");
	Money usd = new Money(Currency.USD, "0.01");
	Money eur = new Money(Currency.EUR, "-1.00");
	Money eur2 = new Money(Currency.EUR, "-1.01");
	assertEquals("PHP 1.00",php.toString());
	assertEquals("USD 0.01",usd.toString());
	assertEquals("EUR -1.00",eur.toString());
	assertEquals("EUR -1.01",eur2.toString());
    }

    @Test
    public void valueTests() {
	Money php = new Money(Currency.PHP, "1.00");
	Money usd = new Money(Currency.USD, "-0.01");
	Money eur = new Money(Currency.EUR, "-1.00");

	assertEquals("1.00",php.getValue());
	assertEquals("-0.01",usd.getValue());
	assertEquals("-1.00",eur.getValue());
    }
    
    @Test
    public void signedAddition()
    {
	Money augend = new Money(Currency.EUR, "-1.20");
	Money addend = new Money(Currency.EUR, "1.01");
	Money result = augend.add(addend);
	Money expected = new Money(Currency.EUR, "-0.19");
	assertEquals(expected, result);

	augend = new Money(Currency.EUR, "2.01");
	addend = new Money(Currency.EUR, "-4.01");
	result = augend.add(addend);
	expected = new Money(Currency.EUR, "-2.00");
	assertEquals(expected, result);
	
	augend = new Money(Currency.EUR, "1.99");
	addend = new Money(Currency.EUR, "4.21");
	result = augend.add(addend);
	expected = new Money(Currency.EUR, "6.20");
	assertEquals(expected, result);
    }
    
    @Test
    public void signedSubtraction()
    {
	Money minuend = new Money(Currency.EUR, "-1.20");
	Money subtrahend = new Money(Currency.EUR, "1.01");
	Money result = minuend.subtract(subtrahend);
	Money expected = new Money(Currency.EUR, -2, -21);
	assertEquals(expected, result);

	minuend = new Money(Currency.EUR, 2, 1);
	subtrahend = new Money(Currency.EUR, -4, -2);
	result = minuend.subtract(subtrahend);
	expected = new Money(Currency.EUR, 6, 3);
	assertEquals(expected, result);
	
	minuend = new Money(Currency.EUR, -5, -9);
	subtrahend = new Money(Currency.EUR, -6, -1);
	result = minuend.subtract(subtrahend);
	expected = new Money(Currency.EUR, 1, 8);
	assertEquals(expected, result);
    }
    
    @Test
    public void sameCurrencyAddition() {
	Money augend = new Money(Currency.EUR, 1, 20);
	Money addend = new Money(Currency.EUR, 1, 1);
	Money result = augend.add(addend);
	Money expected = new Money(Currency.EUR, 2, 21);
	assertEquals(expected, result);

	augend = new Money(Currency.EUR, 1, 0);
	addend = new Money(Currency.EUR, 4, 0);
	result = augend.add(addend);
	expected = new Money(Currency.EUR, 5, 0);
	assertEquals(expected, result);
    }

    @Test
    public void sameCurrencySubtraction() {
	Money minuend = new Money(Currency.USD, 4, 5);
	Money subtrahend = new Money(Currency.USD, 1, 5);
	Money result = minuend.subtract(subtrahend);
	Money expected = new Money(Currency.USD, 3, 0);
	assertEquals(expected, result);
    }

    @Test
    public void sameCurrencySubtractionWithBorrowing() {
	Money minuend = new Money(Currency.USD, 1, 3);
	Money subtrahend = new Money(Currency.USD, 0, 5);
	Money result = minuend.subtract(subtrahend);
	Money expected = new Money(Currency.USD, 0, 8);
	assertEquals(expected, result);

	minuend = new Money(Currency.EUR, 1, 10);
	subtrahend = new Money(Currency.EUR, 0, 11);
	result = minuend.subtract(subtrahend);
	expected = new Money(Currency.EUR, 0, 99);
	assertEquals(expected, result);
    }

    @Test
    public void sameCurrencyAdditionWithCarryDecimal() {
	Money augend = new Money(Currency.PHP, 1, 99);
	Money addend = new Money(Currency.PHP, 1, 1);
	Money result = augend.add(addend);
	Money expected = new Money(Currency.PHP, 3, 0);
	assertEquals(expected, result);

	augend = new Money(Currency.USD, 1, 99);
	addend = new Money(Currency.USD, 0, 99);
	result = augend.add(addend);
	expected = new Money(Currency.USD, 2, 98);
	assertEquals(expected, result);

	augend = new Money(Currency.EUR, 0, 50);
	addend = new Money(Currency.EUR, 0, 50);
	result = augend.add(addend);
	expected = new Money(Currency.EUR, 1, 0);
	assertEquals(expected, result);
    }

    @Test(expected = IncompatibleCurrencyException.class)
    public void incompatibleCurrencyAdditionFromUsdToPhp() {
	Money augend = new Money(Currency.USD, 1, 99);
	Money addend = new Money(Currency.PHP, 1, 1);
	augend.add(addend);
    }

    @Test(expected = IncompatibleCurrencyException.class)
    public void incompatibleCurrencyAdditionFromEURToPhp() {
	Money augend = new Money(Currency.EUR, 1, 99);
	Money addend = new Money(Currency.PHP, 1, 1);
	augend.add(addend);
    }

    @Test(expected = IncompatibleCurrencyException.class)
    public void incompatibleCurrencySubtractionFromEurToUsd() {
	Money augend = new Money(Currency.EUR, 1, 99);
	Money addend = new Money(Currency.USD, 1, 1);
	augend.subtract(addend);
    }

    @Test
    public void nonEqualityTests() {
	Money php1 = new Money(Currency.PHP, 14, 0);
	Money php2 = new Money(Currency.PHP, 14, 2);
	assertFalse(php1.equals(php2));

	Money eur1 = new Money(Currency.EUR, 0, 23);
	Money eur2 = new Money(Currency.EUR, 0, 2);
	assertFalse(eur1.equals(eur2));

	Money usd1 = new Money(Currency.USD, 1, 55);
	Money usd2 = new Money(Currency.USD, 2, 55);
	assertFalse(usd1.equals(usd2));

	Money m1 = new Money(Currency.USD, 1, 11);
	Money m2 = new Money(Currency.PHP, 1, 11);
	assertFalse(m1.equals(m2));
    }

    @Test
    public void moneyEqualsNull() {
	Money money = new Money(Currency.USD, 1, 0);
	assertFalse(money.equals(null));
    }

    @Test
    public void moneyEqualsReflexive() {
	Money money = new Money(Currency.USD, 1, 0);
	assertTrue(money.equals(money) && money.hashCode() == money.hashCode());

	money = new Money(Currency.PHP, 1, 10);
	assertTrue(money.equals(money) && money.hashCode() == money.hashCode());

	money = new Money(Currency.USD, 10, 1);
	assertTrue(money.equals(money) && money.hashCode() == money.hashCode());
    }

    @Test
    public void moneyEqualsSymmetric() {
	Money money1 = (Money) new Money(Currency.EUR, 12, 34);
	Money money2 = (Money) new Money(Currency.EUR, 12, 34);
	assertTrue((money1.equals(money2) && money2.equals(money1))
		&& (money1.hashCode() == money2.hashCode()));
    }

    @Test
    public void moneyEqualsTransitive() {
	Money money1 = (Money) new Money(Currency.USD, 987, 65);
	Money money2 = (Money) new Money(Currency.USD, 987, 65);
	Money money3 = (Money) new Money(Currency.USD, 987, 65);
	assertTrue(money1.equals(money2) && money2.equals(money3)
		&& money1.equals(money3)
		&& money1.hashCode() == money3.hashCode());
    }

    @Test(expected = ArithmeticException.class)
    public void divisionByZero() {
	Money dividend = new Money(Currency.EUR, 1, 0);
	dividend.divide(0);
    }

    @Test
    public void divideByOne() {
	Money dividend = new Money(Currency.EUR, 23, 10);
	Money assumedResult = new Money(Currency.EUR, 23, 10);
	assertTrue(dividend.divide(1).equals(assumedResult));
    }

    @Test
    public void muliplyByZero() {
	Money factor = new Money(Currency.USD, 1, 0);
	Money assumedResult = new Money(Currency.USD, 0, 0);
	assertTrue(factor.multiply(0).equals(assumedResult));
    }

    @Test
    public void muliplyByOne() {
	Money factor = new Money(Currency.USD, 12, 34);
	Money assumedResult = new Money(Currency.USD, 12, 34);
	assertTrue(factor.multiply(1).equals(assumedResult));
    }
}

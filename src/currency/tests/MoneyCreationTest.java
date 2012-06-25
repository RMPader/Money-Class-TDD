package currency.tests;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

import currency.*;
import currency.exceptions.*;

public class MoneyCreationTest {

    @Test
    public void noDecimalCreation() {
	Money noDecimal = new Money("USD -1");
	assertEquals(new BigDecimal("-1.00"), noDecimal.getValue());
	assertEquals("USD -1.00", noDecimal.toString());

	noDecimal = new Money("USD 1");
	assertEquals(new BigDecimal("1.00"), noDecimal.getValue());
	assertEquals("USD 1.00", noDecimal.toString());
    }

    @Test
    public void noWholeNumberCreation() {
	Money noDecimal = new Money("USD .12");
	assertEquals(new BigDecimal(".12"), noDecimal.getValue());
	assertEquals("USD 0.12", noDecimal.toString());

	noDecimal = new Money("USD .1");
	assertEquals(new BigDecimal(".10"), noDecimal.getValue());
	assertEquals("USD 0.10", noDecimal.toString());

	noDecimal = new Money("USD -.12");
	assertEquals(new BigDecimal("-0.12"), noDecimal.getValue());
	assertEquals("USD -0.12", noDecimal.toString());

	noDecimal = new Money("USD -0.02");
	assertEquals(new BigDecimal("-0.02"), noDecimal.getValue());
	assertEquals("USD -0.02", noDecimal.toString());
    }

    @Test
    public void symbolsInValue() {
	try {
	    new Money("PHP 1&.1");
	    new Money("PHP 1.1*");
	    new Money("PHP .1!");
	    new Money("PHP 1^");
	    fail("Must throw an invalid money value exception because whole number contains a non-numeric char");
	} catch (InvalidMoneyValueException e) {
	}
    }

    @Test
    public void characterInDecimal() {
	try {
	    new Money("PHP 1.1a");
	    fail("Must throw an invalid money value exception because decimal contains a non-numeric char");
	} catch (InvalidMoneyValueException e) {
	}
    }

    @Test
    public void multipleDots() {
	try {
	    new Money("PHP 1.10.12");
	    fail("Must throw an invalid money value exception because of multiple decimal points");
	} catch (InvalidMoneyValueException e) {
	}
    }

    @Test
    public void decimalPrecision() {
	try {
	    new Money("PHP 1.001");
	    fail("Must throw an invalid money value exception because decimal is more than 2 precision");
	} catch (InvalidMoneyValueException e) {
	}
    }

    @Test
    public void leadingZeroes() {
	try {
	    Money result = new Money("PHP 000001.01");
	    Money expected = new Money("PHP 1.01");
	    assertEquals(expected, result);
	} catch (InvalidMoneyValueException e) {
	    fail("must not throw exception since leading zeroes should be ignored");
	}
    }

    @Test
    public void trailingZeroes() {
	try {
	    new Money("PHP 1.01000");
	    fail("must throw exception since traling zeroes will not be ignored by the factory");
	} catch (InvalidMoneyValueException e) {
	}
    }

    @Test
    public void createNonExistentType() {
	try {
	    new Money("SGD 1.00");
	    fail("Must throw an invalid money exception because SGD is not included in available currencies");
	} catch (InvalidMoneyTypeException e) {
	}
	try {
	    new Money("HKJASFHKJA 1.00");
	    fail("Must throw an invalid money exception because HKJASFHKJA is not included in available currencies");
	} catch (InvalidMoneyTypeException e) {
	}
    }

    @Test
    public void missingCurrencyType() {
	try {
	    new Money("1.00");
	    fail("Must throw an invalid money exception since currency type is missing");
	} catch (InvalidMoneyTypeException e) {
	}
    }

    @Test
    public void getValuePositiveCurrencies() {
	Money usd = new Money("USD 1.20");
	assertEquals(new BigDecimal("1.20"), usd.getValue());

	usd = new Money("PHP .3");
	assertEquals(new BigDecimal("0.30"), usd.getValue());

	usd = new Money("EUR .02");
	assertEquals(new BigDecimal("0.02"), usd.getValue());

	usd = new Money("USD .12");
	assertEquals(new BigDecimal("0.12"), usd.getValue());

	usd = new Money("EUR .60");
	assertEquals(new BigDecimal("0.60"), usd.getValue());

	usd = new Money("PHP 1");
	assertEquals(new BigDecimal("1.00"), usd.getValue());
    }

    @Test
    public void getValueNegativeCurrencies() {
	Money usd = new Money("USD -1.20");
	assertEquals(new BigDecimal("-1.20"), usd.getValue());

	usd = new Money("PHP -.3");
	assertEquals(new BigDecimal("-0.30"), usd.getValue());

	usd = new Money("EUR -.02");
	assertEquals(new BigDecimal("-0.02"), usd.getValue());

	usd = new Money("USD -.12");
	assertEquals(new BigDecimal("-0.12"), usd.getValue());

	usd = new Money("EUR -.60");
	assertEquals(new BigDecimal("-0.60"), usd.getValue());

	usd = new Money("PHP -1");
	assertEquals(new BigDecimal("-1.00"), usd.getValue());
    }

    @Test
    public void stringOfPositiveCurrencies() {
	Money usd = new Money("USD 1.20");
	assertEquals("USD 1.20", usd.toString());

	usd = new Money("PHP .3");
	assertEquals("PHP 0.30", usd.toString());

	usd = new Money("EUR .02");
	assertEquals("EUR 0.02", usd.toString());

	usd = new Money("USD .12");
	assertEquals("USD 0.12", usd.toString());

	usd = new Money("EUR .60");
	assertEquals("EUR 0.60", usd.toString());

	usd = new Money("PHP 1");
	assertEquals("PHP 1.00", usd.toString());
    }

    @Test
    public void stringOfNegativeCurrencies() {
	Money usd = new Money("USD -1.20");
	assertEquals("USD -1.20", usd.toString());

	usd = new Money("PHP -.3");
	assertEquals("PHP -0.30", usd.toString());

	usd = new Money("EUR -.02");
	assertEquals("EUR -0.02", usd.toString());

	usd = new Money("USD -.12");
	assertEquals("USD -0.12", usd.toString());

	usd = new Money("EUR -.60");
	assertEquals("EUR -0.60", usd.toString());

	usd = new Money("PHP -1");
	assertEquals("PHP -1.00", usd.toString());
    }

    @Test
    public void sameCurrencyDiffValue() {
	Money money1 = new Money("PHP 1");
	Money money2 = new Money("PHP 1.01");
	assertFalse(money1.equals(money2));
    }

    @Test
    public void sameValueDiffCurrency() {
	Money money1 = new Money("PHP 10");
	Money money2 = new Money("USD 10");
	assertFalse(money1.equals(money2));

	money1 = new Money("PHP .1");
	money2 = new Money("USD .1");
	assertFalse(money1.equals(money2));
    }

    @Test
    public void moneyEqualsNull() {
	Money money = new Money("USD 1.00");
	assertFalse(money.equals(null));
    }

    @Test
    public void moneyEqualsReflexive() {
	Money money = new Money("USD 1.00");
	assertTrue(money.equals(money) && money.hashCode() == money.hashCode());

	money = new Money("PHP 1.10");
	assertTrue(money.equals(money) && money.hashCode() == money.hashCode());

	money = new Money("USD 10.01");
	assertTrue(money.equals(money) && money.hashCode() == money.hashCode());
    }

    @Test
    public void moneyEqualsSymmetric() {
	Money money1 = (Money) new Money("EUR 12.34");
	Money money2 = (Money) new Money("EUR 12.34");
	assertTrue((money1.equals(money2) && money2.equals(money1))
		&& (money1.hashCode() == money2.hashCode()));
    }
    
    @Test
    public void moneyEqualsTransitive() {
	Money money1 = (Money) new Money("USD 987.65");
	Money money2 = (Money) new Money("USD 987.65");
	Money money3 = (Money) new Money("USD 987.65");
	assertTrue(money1.equals(money2) && money2.equals(money3)
		&& money1.equals(money3)
		&& money1.hashCode() == money3.hashCode());
    }
}

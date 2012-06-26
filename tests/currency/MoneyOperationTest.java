package currency;

import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class MoneyOperationTest {
    
    @Test
    public void toStringTests(){
	Money php = new Money(Currency.PHP, 1, 0);
	Money usd = new Money(Currency.USD, 0, 1);
	Money eur = new Money(Currency.EUR, -1, 0);
	
	assertTrue(php.toString().equals("PHP 1.00"));
	assertTrue(usd.toString().equals("USD 0.01"));
	assertTrue(eur.toString().equals("EUR -1.00"));
    }
    
    @Test(expected=ArithmeticException.class)
    public void divisionByZero() {
	Money dividend = new Money(Currency.EUR, 1, 0);
	dividend.divide(0);
    }
    
    @Test
    public void divideByOne(){
	Money dividend = new Money(Currency.EUR, 23, 10);
	Money assumedResult = new Money(Currency.EUR, 23, 10);
	assertTrue(dividend.divide(1).equals(assumedResult));
    }
    
    @Test
    public void muliplyByZero(){
	Money factor = new Money(Currency.USD, 1, 0);
	Money assumedResult = new Money(Currency.USD, 0, 0);
	assertTrue(factor.multiply(0).equals(assumedResult));
    }
    
    @Test
    public void muliplyByOne(){
	Money factor = new Money(Currency.USD, 12, 34);
	Money assumedResult = new Money(Currency.USD, 12, 34);
	assertTrue(factor.multiply(1).equals(assumedResult));
    }
}

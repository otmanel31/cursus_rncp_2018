package com.loncoto.firstJunitForm;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

// cette classe contiendra un ensemble de texte a executer
// en Junit plus "ancien", on pouvais heriter de TestCase
public class CalculatriceTest {
	
	// la classe/objet à tester en isolation
	private Calculatrice c;
	
	
	@BeforeClass
	public static void beforeAllTest() {
		System.out.println("preparation initiale pour CalculatriceTest");
	}
	
	@AfterClass
	public static void afterAllTest() {
		System.out.println("nettoyage final pour CalculatriceTest");
	}
	
	
	// cette méthode sera appelée avant toute execution de test
	// individuelle
	// elle sert à "préparer" l'environnement de test pour un
	// test individuel
	@Before
	public void beforeTest() {
		System.out.println("préparation avant un test");
		c = new Calculatrice();
	}
	
	// cette méthode permet de "nettoyer" l'environnement de test
	// après l'execution d'un test individuel
	@After
	public void afterTest() {
		System.out.println("après un test, nettoyage");
		c = null;
	}
	
	// une méthode annotée avec @test
	// est un test junit 4.*
	// ce test echouera si une des assertions du test échoue
	// une assertion qui echoue levera une exception spécifique
	// qui sera attrapée par le framewor junit
	// les Assertions sont fournies par junit, il existe des librairies etendant celles-ci
	@Test
	public  void testAddition() {
		int expected = 36;
		int actual = c.addition(10, 26);
		assertEquals("10 + 26 devrait donner 36! ",expected, actual);
		expected = 43;
		actual = c.addition(10, 33);
		assertEquals("10 + 33 devrait donner 43! ",expected, actual);
	}
	
	@Test
	public void testDivision() {
		int expected = 5;
		int actual = c.division(10, 2);
		assertEquals("10 / 2 devrait donner 5",expected, actual);
	}
	
	@Test
	public void testDivisionArrondi() {
		int expected = 3;
		int actual = c.division(10, 3);
		assertEquals("10 / 3 devrait donner 3",expected, actual);
	}
	
	// ce test réussi si une arithmetic excpetion est bien déclenchée
	@Test(expected=ArithmeticException.class)
	public void testDivisionZero() {
		int actual = c.division(10, 0);
	}
	
	
	@Test
	public void testMultiplicationDouble() {
		double expected = 3500000.0;
		double actual = c.multiplication(1000.0, 3500.0);
		//assertEquals("1000 * 3500 devrait donner 3500000",expected, actual);
		assertEquals("multiplication devrait donner 3500000 environ" ,
						expected,
						actual,
						0.0001);
	}
	
	// si ce test ne finit pas en 0.5 secondes, il echoue
	@Test(timeout=500)
	public void testCalculLong() {
		double expected = 2.0;
		double actual = c.calculComplexeEtlent(1.0);
		assertEquals("calcul devrait etre 2.0", expected, actual, 0.0000001);
	}
	
	
	
	
	
}

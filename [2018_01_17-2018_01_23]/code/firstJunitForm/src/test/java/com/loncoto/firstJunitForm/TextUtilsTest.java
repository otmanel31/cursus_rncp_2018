package com.loncoto.firstJunitForm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class TextUtilsTest {
	private TextUtils tu;
	
	@Before
	public void beforeTest() {
		tu = new TextUtils();
	}
	
	@After
	public void afterTest() {
		tu = null;
	}
	
	@Category(FastTest.class)
	@Test
	public void testCapitaliseNormal() {
		String expected = "Vincent courtalon";
		String actual = tu.capitalise("vincent courtalon");
		assertEquals("premier caractere devrait etre en majuscule", expected, actual);
	}

	@Category(FastTest.class)
	@Test
	public void testInverseNormal() {
		String expected = "ruojnob";
		String actual = tu.inverse("bonjour");
		assertEquals("bonjour devrais etre inverse", expected, actual);
	}
	
	@Category(FastTest.class)
	@Test
	public void testCapitaliseVide() {
		String expected = "";
		String actual = tu.capitalise("");
		assertEquals("devrait etre une chaine", expected, actual);
	}
	
	@Test
	public void testCapitaliseNull() {
		String actual = tu.capitalise(null);
		assertNull("la chaine devrait etre null si null en entree", actual);
	}
	
	@Test
	public void testInverseNull() {
		String actual = tu.inverse(null);
		assertNull("la chaine devrait etre null si null en entree", actual);
	}
	
}

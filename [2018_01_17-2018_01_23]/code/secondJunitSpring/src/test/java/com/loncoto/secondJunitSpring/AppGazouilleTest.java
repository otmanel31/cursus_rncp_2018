package com.loncoto.secondJunitSpring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.*;

import com.loncoto.secondJunitSpring.metier.Gazouille;
import com.loncoto.secondJunitSpring.repositories.GazouilleDaoMock;
import com.loncoto.secondJunitSpring.services.GazouilleService;
import com.loncoto.secondJunitSpring.services.GazouilleService.GazouilleNotFoundException;

// utilise notre contexte special test avec le mock DAO
// s'execute dans un contexte spring
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:testContext.xml")
public class AppGazouilleTest {
	
	@Autowired
	private GazouilleService gazouilleService;
	
	@Autowired
	private GazouilleDaoMock gazouilleDaoMock;
	
	@Before
	public void beforeTest() {
		this.gazouilleDaoMock.addGazouilles(
				new Gazouille(1, "premiere gazouille", "blahblah 1")
				,new Gazouille(2, "deuxieme gazouille", "blahblah 2")
				,new Gazouille(3, "troisieme gazouille", "blahblah 3"));
	}

	
	@Test
	public void testCountGazouille() {
		int expected = 3;
		int actual = this.gazouilleService.readAllGazouille().size();
		assertEquals("devrait compter 3 gazouilles", expected, actual);
		
		expected = 4;
		this.gazouilleService.publish(new Gazouille(0, "hoho", "hihi"));
		actual = this.gazouilleService.readAllGazouille().size();
		assertEquals("devrait compter 4 gazouilles", expected, actual);
	}

	@Test
	public void testPublishGazouilleFind() {
		Gazouille g = this.gazouilleService.readGazouille(2);
		assertNotNull("devrait trouver gazouille 2", g);
		assertEquals("id devrait etre a 2", 2, g.getId());
		assertEquals("titre devrait etre 'deuxieme gazouille'", "deuxieme gazouille", g.getTitre());
		
		this.gazouilleService.publish(new Gazouille(0, "une autre", "blah"));
		g = this.gazouilleService.readGazouille(4);
		assertNotNull("devrait trouver gazouille 4", g);
	}
	
	@Test
	public void testPublishGazouilleCensure() {
		String expected = "vive gazouille";
		this.gazouilleService.publish(new Gazouille(0, "vive twitter", "vive twitter"));
		Gazouille g = gazouilleService.readGazouille(4);
		assertEquals("twitter devrait etre remplace par gazouille dans le titre",
					 expected, g.getTitre());
		assertEquals("twitter devrait etre remplace par gazouille dans le corps",
				 expected, g.getCorps());
	
	}
	
	@Test
	public void testPublishGazouilleCensureAvance() {
		String expected = "vive gazouille";
		this.gazouilleService.publish(new Gazouille(0, "vive Twitter", "vive TWITTER"));
		Gazouille g = gazouilleService.readGazouille(4);
		assertEquals("twitter devrait etre remplace par gazouille dans le titre",
					 expected, g.getTitre());
		assertEquals("twitter devrait etre remplace par gazouille dans le corps",
				 expected, g.getCorps());
	
	}
	
	@Test(expected=GazouilleNotFoundException.class)
	public void testGazouilleNotFound() {
		this.gazouilleService.readGazouille(5);
	}
	
	
}


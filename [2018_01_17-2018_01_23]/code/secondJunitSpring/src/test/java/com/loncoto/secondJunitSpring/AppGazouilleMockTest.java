package com.loncoto.secondJunitSpring;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.loncoto.secondJunitSpring.config.TestConfigMock;
import com.loncoto.secondJunitSpring.metier.Gazouille;
import com.loncoto.secondJunitSpring.repositories.GazouilleDao;
import com.loncoto.secondJunitSpring.services.GazouilleService;

// importe les methodes static de la Classe Mockito
import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= TestConfigMock.class)
public class AppGazouilleMockTest {

	private GazouilleService gazouilleService;
	
	@Autowired
	private GazouilleDao gazouilleDAO;
	
	@Before
	public void prepareTest() {
		// instanciation du service
		this.gazouilleService = new GazouilleService();
		// et injection "a la main" du dao Mock dedans
		this.gazouilleService.setGazouilleDao(this.gazouilleDAO);
	}
	@After
	public void cleanAfterTest() {
		// reinitialise le mock
		reset(this.gazouilleDAO);
	}
	
	@Test
	public void gazouilleTestCount() {
		when(gazouilleDAO.findAll()).thenReturn(
				Arrays.asList(new Gazouille(1, "first", "first"),
							  new Gazouille(2, "second", "second")));
		
		int expected = 2;
		int actual = this.gazouilleService.readAllGazouille().size();
		assertEquals("should return 2 gazouille", expected, actual);
		// verifie que FindAll a été appelé au moins une fois
		verify(gazouilleDAO, Mockito.atLeastOnce()).findAll();
	}
	
}

package com.loncoto.secondJunitSpring.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.loncoto.secondJunitSpring.repositories.GazouilleDao;
import com.loncoto.secondJunitSpring.services.GazouilleService;

@Configuration
public class TestConfigMock {
	
	@Bean
	public GazouilleDao gazouilleDao() {
		// renvoyer un "faux" DAO qui ressemble
		// au GazouilleDAO
		return Mockito.mock(GazouilleDao.class);
	}
	
}

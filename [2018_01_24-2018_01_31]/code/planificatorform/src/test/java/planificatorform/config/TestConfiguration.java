package planificatorform.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.loncoto.planificatorform.repositories.IntervenantRepository;
import com.loncoto.planificatorform.repositories.InterventionRepository;

@Configuration
public class TestConfiguration {

	@Bean
	@Primary
	public InterventionRepository interventionRepository() {
		return Mockito.mock(InterventionRepository.class);
	}
	
	@Bean
	@Primary
	public IntervenantRepository intervenantRepository() {
		return Mockito.mock(IntervenantRepository.class);
	}
	
	
}

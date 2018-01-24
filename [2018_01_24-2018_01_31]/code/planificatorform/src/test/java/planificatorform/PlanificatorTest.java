package planificatorform;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.loncoto.planificatorform.metier.Intervenant;
import com.loncoto.planificatorform.metier.Intervention;
import com.loncoto.planificatorform.repositories.IntervenantRepository;
import com.loncoto.planificatorform.repositories.InterventionRepository;
import com.loncoto.planificatorform.services.PlanificatorService;
import com.loncoto.planificatorform.services.PlanificatorService.IntervenantOccupeException;
import com.loncoto.planificatorform.services.PlanificatorService.InvalidInterventionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import planificatorform.config.TestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfiguration.class)
public class PlanificatorTest {

	@Autowired
	private InterventionRepository interventionRepository;
	
	@Autowired
	private IntervenantRepository intervenantRepository;
	
	
	private PlanificatorService planificatorService;
	
	
	private List<Intervenant> getSampleIntervenants() {
		return new ArrayList<>(Arrays.asList(
				new Intervenant(1, "bob", "bob@bikinibottom.com"),
				new Intervenant(2, "patrick", "patrick@bikinibottom.com")));
		
	}
	private List<Intervention> getSampleInterventions() {
		Intervenant i1 = new Intervenant(1, "bob", "bob@bikinibottom.com");
		Intervenant i2 = new Intervenant(2, "patrick", "patrick@bikinibottom.com");
		List<Intervention> data = new ArrayList<>();
		Intervention inter = new Intervention(1, "porte maillot",
				LocalDateTime.of(2018, 1, 10, 10, 0),
				LocalDateTime.of(2018, 1, 10, 12, 0),
				42);
		inter.setIntervenant(i1);
		data.add(inter);

		inter = new Intervention(2, "neuilly",
				LocalDateTime.of(2018, 1, 10, 14, 0),
				LocalDateTime.of(2018, 1, 10, 16, 0),
				25);
		inter.setIntervenant(i1);
		data.add(inter);
		
		inter = new Intervention(3, "porte maillot",
				LocalDateTime.of(2018, 1, 15, 8, 0),
				LocalDateTime.of(2018, 1, 15, 11, 0),
				42);
		inter.setIntervenant(i2);
		data.add(inter);
		
		inter = new Intervention(4, "la rochelle",
				LocalDateTime.of(2018, 1, 15, 14, 30),
				LocalDateTime.of(2018, 1, 15, 17, 30),
				27);
		inter.setIntervenant(i2);
		data.add(inter);
		
		return data;
	}
	
	
	
	@Before
	public void beforeTest() {
		planificatorService = new PlanificatorService();
		//intervenantRepository
		planificatorService.setIntervenantRepository(intervenantRepository);
		planificatorService.setInterventionRepository(interventionRepository);
		
	}
	
	@After
	public void afterTest() {
		reset(intervenantRepository, interventionRepository);
	}
	
	@Test
	public void testListeNormale() {
		when(interventionRepository.findAll(new PageRequest(0, 10)))
					.thenReturn(new PageImpl<>(getSampleInterventions(),
								new PageRequest(0, 10),
								4));
		
		Page<Intervention> p = planificatorService.listeIntervention(new PageRequest(0, 10));
		assertEquals("should return 4 elements", 4, p.getContent().size());
		assertEquals("first intervention should have id 1", 1, p.getContent().get(0).getId());
		
		// est ce que findAll a été appelé une fois au moins
		verify(interventionRepository, atLeastOnce()).findAll(any(Pageable.class));
	}
	
	@Test
	public void testPlanifOk() {
		// aucune collisions potentielles
		when(interventionRepository.findByIntervenantIdAndDebutAfterAndDebutBefore(
				eq(1),  // int = 1
				any(LocalDateTime.class),
				any(LocalDateTime.class)
				))
					.thenReturn(new ArrayList<>());
		
		Intervention inter = new Intervention(5, "toulouse", LocalDateTime.of(2018, 1, 12, 14, 0),
				LocalDateTime.of(2018, 1, 12, 16, 0), 45);
		inter.setIntervenant(getSampleIntervenants().get(0));
		
		when(interventionRepository.save(inter)).thenReturn(inter);
		
		Intervention result = planificatorService.planifie(inter);
		
		assertEquals("sould be the same intervention", inter, result);
		verify(interventionRepository, atLeastOnce()).save(inter);
		
	}
	
	@Test
	public void testPlanifOkDur() {
		// aucune collisions potentielles
		when(interventionRepository.findByIntervenantIdAndDebutAfterAndDebutBefore(
				eq(1),  // int = 1
				any(LocalDateTime.class),
				any(LocalDateTime.class)
				))
					.thenReturn(
					getSampleInterventions().stream()
											.filter(i -> i.getIntervenant().getId() == 1)
											.collect(Collectors.toList()));
		
		Intervention inter = new Intervention(5, "porte maillot",
				LocalDateTime.of(2018, 1, 10, 12, 30),
				LocalDateTime.of(2018, 1, 10, 13, 30), 46);
		inter.setIntervenant(getSampleIntervenants().get(0));
		
		when(interventionRepository.save(inter)).thenReturn(inter);
		
		Intervention result = planificatorService.planifie(inter);
		
		assertEquals("sould be the same intervention", inter, result);
		verify(interventionRepository, atLeastOnce()).save(inter);
		
	}
	
	@Test(expected=IntervenantOccupeException.class)
	public void testPlanifKoOccupe() {
		
		when(interventionRepository.findByIntervenantIdAndDebutAfterAndDebutBefore(
				eq(1),  // int = 1
				any(LocalDateTime.class),
				any(LocalDateTime.class)
				))
					.thenReturn(
					getSampleInterventions().stream()
											.filter(i -> i.getIntervenant().getId() == 1)
											.collect(Collectors.toList()));
		
		Intervention inter = new Intervention(5, "porte maillot",
				LocalDateTime.of(2018, 1, 10, 12, 30),
				LocalDateTime.of(2018, 1, 10, 15, 30), 46);
		inter.setIntervenant(getSampleIntervenants().get(0));
		
		Intervention result = planificatorService.planifie(inter);
		
	}
	
	@Test(expected=InvalidInterventionException.class)
	public void testPlanifKoInvalidePasIntervenant() {
		
		
		Intervention inter = new Intervention(5, "porte maillot",
				LocalDateTime.of(2018, 1, 20, 12, 30),
				LocalDateTime.of(2018, 1, 20, 15, 30), 46);
		
		Intervention result = planificatorService.planifie(inter);
		
	}

	@Test(expected=InvalidInterventionException.class)
	public void testPlanifKoInvalideFinAvantDebut() {
		
		
		Intervention inter = new Intervention(5, "porte maillot",
				LocalDateTime.of(2018, 1, 20, 15, 30),
				LocalDateTime.of(2018, 1, 20, 12, 30), 46);
		
		Intervention result = planificatorService.planifie(inter);
	}
	
	@Test(expected=InvalidInterventionException.class)
	public void testPlanifKoInvalideTropLong() {
		
		
		Intervention inter = new Intervention(5, "porte maillot",
				LocalDateTime.of(2018, 1, 20, 10, 30),
				LocalDateTime.of(2018, 1, 20, 14, 45), 46);
		
		Intervention result = planificatorService.planifie(inter);
	}
	
}

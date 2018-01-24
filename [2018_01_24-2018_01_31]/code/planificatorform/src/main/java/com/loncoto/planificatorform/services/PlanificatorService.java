package com.loncoto.planificatorform.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.loncoto.planificatorform.metier.Intervenant;
import com.loncoto.planificatorform.metier.Intervention;
import com.loncoto.planificatorform.repositories.IntervenantRepository;
import com.loncoto.planificatorform.repositories.InterventionRepository;

@Service
public class PlanificatorService {

	@Autowired
	private InterventionRepository interventionRepository;
	@Autowired
	private IntervenantRepository intervenantRepository;
	
	public void setInterventionRepository(InterventionRepository interventionRepository) {
		this.interventionRepository = interventionRepository;
	}
	public void setIntervenantRepository(IntervenantRepository intervenantRepository) {
		this.intervenantRepository = intervenantRepository;
	}

	public Intervention planifie(Intervention newIntervention) {
		if (newIntervention == null || newIntervention.getIntervenant() == null)
			throw new InvalidInterventionException("intervention ne peut etre vide ou sans intervenant");
		if (newIntervention.getDebut().isAfter(newIntervention.getFin()))
			throw new InvalidInterventionException("voyage dans le temps non implemente");
		long duration = (newIntervention.getDebut()
										.until(newIntervention.getFin(), ChronoUnit.MINUTES));
		if (duration < 30 || duration > 240)
			throw new InvalidInterventionException("duree de l'intervention invalide");
		
		List<Intervention> conflictPossible = interventionRepository
				.findByIntervenantIdAndDebutAfterAndDebutBefore(
						newIntervention.getIntervenant().getId(),
						newIntervention.getDebut().minusDays(1),
						newIntervention.getDebut().plusDays(1));
		for (Intervention intervention : conflictPossible) {
			if (intervention.getFin().isBefore(newIntervention.getDebut()) ||
				intervention.getDebut().isAfter(newIntervention.getFin())) {
				continue;
			}
			else {
				throw new IntervenantOccupeException("l'intervenant est deja occupe");
			}
		}
		return interventionRepository.save(newIntervention);
	}
	
	public Page<Intervention> listeIntervention(Intervenant intervenant, Pageable page) {
		if (intervenant == null)
			throw new InvalidInterventionException("pas d'intervenant specifie");
		return interventionRepository.findByIntervenantId(intervenant.getId(), page);
	}
	
	public Page<Intervention> listeIntervention(Pageable page) {
		return interventionRepository.findAll(page);
	}
	
	public Page<Intervention> listeInterventionFutur(LocalDateTime from, int noMateriel, Pageable page) {
		return interventionRepository.findByNoMaterielAndDebutAfter(noMateriel, from, page);
	}

	// erreur custom
	public static class InvalidInterventionException extends RuntimeException {
		public InvalidInterventionException(String message) {
			super(message);
		}
	}
	
	
	// erreur custom
	public static class IntervenantOccupeException extends RuntimeException {
		public IntervenantOccupeException(String message) {
			super(message);
		}
	}
	
}

package com.loncoto.planificatorform.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.loncoto.planificatorform.metier.Intervention;

public interface InterventionRepository extends PagingAndSortingRepository<Intervention, Integer> {

	List<Intervention> findByIntervenantIdAndDebutAfterAndDebutBefore(int intervenantId,
																	  LocalDateTime start,
																	  LocalDateTime end);
	Page<Intervention> findByIntervenantId(int intervenantId, Pageable page);
	Page<Intervention> findByNoMaterielAndDebutAfter(int noMateriel,
												LocalDateTime start,
												Pageable page);
}

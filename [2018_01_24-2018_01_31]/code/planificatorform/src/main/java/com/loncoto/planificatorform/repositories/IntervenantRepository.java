package com.loncoto.planificatorform.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.loncoto.planificatorform.metier.Intervenant;

public interface IntervenantRepository extends PagingAndSortingRepository<Intervenant, Integer> {

}

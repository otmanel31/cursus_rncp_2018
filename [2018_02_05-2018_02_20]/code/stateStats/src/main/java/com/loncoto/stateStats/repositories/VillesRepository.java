package com.loncoto.stateStats.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.loncoto.stateStats.metier.Villes;


@RepositoryRestResource(path="villes")
public interface VillesRepository extends MongoRepository<Villes, String>
{

	Page<Villes> findByPopGreaterThan(int popMin, Pageable p);
}

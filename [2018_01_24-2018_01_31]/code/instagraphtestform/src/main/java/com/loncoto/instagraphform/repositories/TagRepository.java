package com.loncoto.instagraphform.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.loncoto.instagraphform.metier.Tag;

@RepositoryRestResource
public interface TagRepository extends PagingAndSortingRepository<Tag, Integer> {

}

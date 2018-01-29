package com.loncoto.instagraphform.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.loncoto.instagraphform.metier.Tag;

@RepositoryRestResource
@CrossOrigin(origins="http://localhost:4200")
public interface TagRepository extends PagingAndSortingRepository<Tag, Integer> {

	List<Tag> findByContents_id(long id);
	
	Page<Tag> findByLibelleContaining(String searchTerm, Pageable page);
}

package com.loncoto.instagraphform.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.loncoto.instagraphform.metier.Image;

public interface ImageRepository extends PagingAndSortingRepository<Image, Long>,
										 ImageRepositoryCustom {
	

}

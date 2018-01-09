package com.loncoto.instagraphform.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.loncoto.instagraphform.metier.Content;

public interface ContentRepository extends PagingAndSortingRepository<Content, Long> {

}

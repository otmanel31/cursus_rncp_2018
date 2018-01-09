package com.loncoto.instagraphform.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.loncoto.instagraphform.metier.Tag;

public interface TagRepository extends PagingAndSortingRepository<Tag, Integer> {

}

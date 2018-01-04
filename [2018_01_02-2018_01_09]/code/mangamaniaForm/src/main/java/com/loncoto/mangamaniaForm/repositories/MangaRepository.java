package com.loncoto.mangamaniaForm.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.loncoto.mangamaniaForm.metier.Manga;

public interface MangaRepository extends PagingAndSortingRepository<Manga, Integer>
{
	List<Manga> findByTitreContaining(String titre);
}

package com.loncoto.notflix.repositories;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.loncoto.notflix.metier.Film;

public interface FilmDepot {

	List<Film> findAll();

	Film save(Film f);

	Film findById(int id);

	void delete(int id);

}
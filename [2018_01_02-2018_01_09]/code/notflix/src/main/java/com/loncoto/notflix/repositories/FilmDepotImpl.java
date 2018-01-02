package com.loncoto.notflix.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loncoto.notflix.metier.Film;

@Service
public class FilmDepotImpl implements FilmDepot {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional(readOnly=true)
	public List<Film> findAll() {
		return em.createQuery("select f from Film as f", Film.class)
				 .getResultList();
	}
	
	@Override
	@Transactional
	public Film save(Film f) {
		if (f.getId() == 0)
			em.persist(f);
		else
			f = em.merge(f);
		return f;
	}
	
	@Override
	@Transactional
	public Film findById(int id) {
		return em.find(Film.class, id);
	}
	
	@Override
	@Transactional
	public void delete(int id) {
		Film f = em.find(Film.class, id);
		if (f != null)
			em.remove(f);
	}
	
	
}

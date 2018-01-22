package com.loncoto.secondJunitSpring.repositories;

import java.util.List;

import com.loncoto.secondJunitSpring.metier.Gazouille;

public interface GazouilleDao {

	List<Gazouille> findAll();

	Gazouille findById(int id);

	int save(Gazouille gazouille);

}
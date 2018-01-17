package com.loncoto.instagraphform.repositories;

import org.springframework.data.repository.CrudRepository;

import com.loncoto.instagraphform.metier.Utilisateur;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Integer> {

	Utilisateur findByUsername(String username);
}

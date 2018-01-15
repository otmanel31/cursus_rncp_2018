package com.loncoto.firstsecurityform.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.loncoto.firstsecurityform.metier.Utilisateur;

public interface UtilisateurRepository extends PagingAndSortingRepository<Utilisateur, Integer> {

}

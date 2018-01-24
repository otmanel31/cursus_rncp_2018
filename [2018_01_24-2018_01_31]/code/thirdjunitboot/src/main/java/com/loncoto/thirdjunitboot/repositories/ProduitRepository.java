package com.loncoto.thirdjunitboot.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.loncoto.thirdjunitboot.metier.Produit;

public interface ProduitRepository extends PagingAndSortingRepository<Produit, Integer> {

	Page<Produit> findByCategorie(String categorie, Pageable page);
}

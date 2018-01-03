package com.loncoto.funspy.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.loncoto.funspy.metier.Article;

public interface ArticleRepository extends CrudRepository<Article, Integer>{

	List<Article> findByNomContaining(String nom);
	List<Article> findByDateSortieBefore(LocalDate dateSortie);
	
	@Query(value="select a from Article as a where a.prix < :promotion")
	List<Article> trouverPromotion(double promotion);
	
}

package com.loncoto.thirdjunitboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.loncoto.thirdjunitboot.metier.Produit;
import com.loncoto.thirdjunitboot.repositories.ProduitRepository;
import com.loncoto.thirdjunitboot.web.ProduitController;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize; // test sur du contenu json

import java.util.ArrayList;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers=ProduitController.class)
@EnableSpringDataWebSupport  // activer le support spring data, comme par exemple la pagination
public class ThirdjunitbootApplicationTests {

	
	/*
	 * @WebMvcTest vas nous permettre de tester un controller spring mvc
	 * dans un "faux" environnement web simulé (trest pratique pour les tests unitaires)
	 * 
	 * on pourra envoyer des requette http "virtuelle" a notre controller, et inspecter
	 * la réponse qu'il retourne (comme du json par exemple)
	 * tout cela sans réeelement démarrer un serveru web (d'ou faciliter d'execution/config)
	 * 
	 *   								gestion des mapping (/, "/produits", ...)
	 *   +---------MockMvc-------------------+
	 *   |					<--http	<--	 	 |<---- envoie requette virtuelle de test
	 *   |   ProduitController  			 |
	 *   |					--> json,etc --> |--->  verification des donnees renvoyée
	 *   +-----------------------------------+
	 * 
	 */
	@Autowired
	private MockMvc mockMvc;
	
	// injectera automatiquement un Mock (via Mockito) du repository
	// c'est un ajout qui fonctionne automatiquement en spring boot
	@MockBean
	private ProduitRepository produitRepository;
	
	
	private Page<Produit> getSampleProduitPage1() {
		return new PageImpl<>(
				new ArrayList<>(Arrays.asList(
						new Produit(1, "test1", 15.5, 0.75, 10, "cat1"),
						new Produit(2, "test2", 25.5, 1.75, 3, "cat2"),
						new Produit(3, "test3", 35.5, 0.50, 20, "cat1"))),
				new PageRequest(0, 10),
				3);
	}
	
	
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testListe() throws Exception{
		when(produitRepository.findAll(any(Pageable.class)))
							  .thenReturn(getSampleProduitPage1());
		
		mockMvc.perform(get("/produits/"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.content", hasSize(3)));
		
		verify(produitRepository, atLeastOnce()).findAll(any(Pageable.class));
	}

}

package com.loncoto.thirdjunitboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
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
import static org.hamcrest.Matchers.equalTo; // test sur du contenu json
import static org.hamcrest.Matchers.closeTo; // test sur du contenu json

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
	
	@Test
	public void testSaveProduit() throws Exception {
		Produit p = new Produit(4, "steak de bison", 15.5, 0.5, 4, "boucherie");
		//Produit p2 = new Produit(4, "steak de bison", 15.7, 0.5, 4, "boucherie");
		String produitJson = "{\n" + 
				"   \"id\": 4,\n" + 
				"   \"nom\": \"steak de bison\",\n" + 
				"   \"prix\": 15.5,\n" + 
				"   \"poids\": 0.5,\n" + 
				"   \"stock\": 4,\n" + 
				"   \"categorie\": \"boucherie\"\n" + 
				"}";
		
		when(produitRepository.save(any(Produit.class)))
							  .thenReturn(p);
		mockMvc.perform(post("/produits/")
						  .content(produitJson)
						  .contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", equalTo(4)))
				.andExpect(jsonPath("$.nom", equalTo("steak de bison")))
				.andExpect(jsonPath("$.prix", closeTo(15.5, 0.0001)))
				.andExpect(jsonPath("$.poids", closeTo(0.5, 0.0001)))
				.andExpect(jsonPath("$.stock", equalTo(4)))
				.andExpect(jsonPath("$.categorie", equalTo("boucherie")));
		
		// save appelé au moins une fois et avec le bon produit
		verify(produitRepository, atLeastOnce()).save(
				argThat(new ProduitEqualityMatcher(p)));
	}
	
	@Test
	public void testSaveProduitPrixKo() throws Exception {
		Produit p = new Produit(4, "steak de bison", -15.5, 0.5, 4, "boucherie");
		Produit p2 = new Produit(4, "steak de bison", 0.0, 0.5, 4, "boucherie");
		String produitJson = "{\n" + 
				"   \"id\": 4,\n" + 
				"   \"nom\": \"steak de bison\",\n" + 
				"   \"prix\": -15.5,\n" + 
				"   \"poids\": 0.5,\n" + 
				"   \"stock\": 4,\n" + 
				"   \"categorie\": \"boucherie\"\n" + 
				"}";
		
		when(produitRepository.save(any(Produit.class)))
							  .thenReturn(p2);
		mockMvc.perform(post("/produits/")
						  .content(produitJson)
						  .contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", equalTo(4)))
				.andExpect(jsonPath("$.nom", equalTo("steak de bison")))
				.andExpect(jsonPath("$.prix", closeTo(0.0, 0.0001)))
				.andExpect(jsonPath("$.poids", closeTo(0.5, 0.0001)))
				.andExpect(jsonPath("$.stock", equalTo(4)))
				.andExpect(jsonPath("$.categorie", equalTo("boucherie")));
		
		// save appelé au moins une fois et avec le bon produit avec le prix remis à 0
		verify(produitRepository, atLeastOnce()).save(
				argThat(new ProduitEqualityMatcher(p2)));
	}
	
	@Test
	public void testSaveProduitPoidsKo() throws Exception {
		String produitJson = "{\n" + 
				"   \"id\": 4,\n" + 
				"   \"nom\": \"steak de bison\",\n" + 
				"   \"prix\": 15.5,\n" + 
				"   \"poids\": -0.5,\n" + 
				"   \"stock\": 4,\n" + 
				"   \"categorie\": \"boucherie\"\n" + 
				"}";
		mockMvc.perform(post("/produits/")
						  .content(produitJson)
						  .contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.fieldError", equalTo("poids")))
				.andExpect(jsonPath("$.errorMessage", equalTo("le poids ne peut etre negatif")))
				.andExpect(jsonPath("$.entite.id", equalTo(4)))
				.andExpect(jsonPath("$.entite.nom", equalTo("steak de bison")));
		
		// save n'est pas appelé a cause du poids invalide
		verify(produitRepository, never()).save(any(Produit.class));
	}
	
	/*
	 * cette classe va permettre a mockito de comparer si 2 produits sont les mêmes
	 * a partir de leur contenu
	 * 
	 * pour les valeur double, on verifie si la différence entre les deux
	 * est suffisament petite pour etre considéré comme égale
	 *  (car les doubles sont des valeur arrondies)
	 * on utilise Abs pour ne pas s'occuper du signe de la différence
	 * 
	 * cette classe Matcher comparera le produit p que l'on lui indique
	 * a celui passé en paremetre à vérifier
	 */
	public static class ProduitEqualityMatcher extends ArgumentMatcher<Produit> {

		private Produit p;
		
		public ProduitEqualityMatcher(Produit p) {
			this.p = p;
		}

		@Override
		public boolean matches(Object argument) {
			if (!(argument instanceof Produit)) return false;
			Produit other = (Produit)argument;
			return p.getId() == other.getId() 
					&& p.getNom().equals(other.getNom())
					&& p.getCategorie().equals(other.getCategorie())
					&& Math.abs(p.getPrix() - other.getPrix()) < 0.0001
					&& Math.abs(p.getPoids() - other.getPoids()) < 0.0001
					&& p.getStock() == other.getStock();
		}
		
	}
}

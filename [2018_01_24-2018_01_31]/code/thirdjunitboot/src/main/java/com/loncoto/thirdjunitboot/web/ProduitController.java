package com.loncoto.thirdjunitboot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.loncoto.thirdjunitboot.metier.Produit;
import com.loncoto.thirdjunitboot.repositories.ProduitRepository;

@Controller
@RequestMapping("/produits")
public class ProduitController {

	@Autowired
	private ProduitRepository produitRepository;
	
	
	@RequestMapping(value="/",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Page<Produit> liste(@PageableDefault(page=0,size=10) Pageable page) {
		return produitRepository.findAll(page);
	}
	
	@RequestMapping(value="/cat/{categorie:.+}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Page<Produit> listeByCategorie(
			@PathVariable("categorie") String categorie,
			@PageableDefault(page=0,size=10) Pageable page) {
		return produitRepository.findByCategorie(categorie, page);
	}

	@RequestMapping(value="/",
			method=RequestMethod.POST,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Produit saveProduit(@RequestBody Produit produit) {
		return produitRepository.save(produit);
	}

	
}

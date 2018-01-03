package com.loncoto.funspy.web;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.loncoto.funspy.metier.Article;
import com.loncoto.funspy.repositories.ArticleRepository;

@Controller
@RequestMapping(value="/")
public class IndexController {

	@Autowired
	private ArticleRepository articleRepository;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectToIndex() {
		return "redirect:/index";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView liste(@RequestParam(value="searchTerm") Optional<String> searchTerm) {
		ModelAndView mv = new ModelAndView("liste");
		if (searchTerm.isPresent())
			mv.addObject("articles", articleRepository.findByNomContaining(searchTerm.get()));
		else
			mv.addObject("articles", articleRepository.findAll());
		return mv;
	}
	
	@RequestMapping(value="/saveArticle", method= RequestMethod.POST)
	public String saveArticle(
							 @RequestParam(value="id", defaultValue="0") int id,
							 @RequestParam("nom") String nom,
							 @RequestParam("description") String description,
							 @RequestParam(value="prix", defaultValue="0.0") double prix,
							 @RequestParam(value="poids", defaultValue="0.0") double poids) {
		
		Article art = null;
		if (id == 0) {
			art = new Article(0, nom, description, prix, poids, LocalDate.now());
		}
		else {
			art = articleRepository.findOne(id);
			if (art == null) return "redirect:/index";
			art.setNom(nom);
			art.setDescription(description);
			art.setPrix(prix);
			art.setPoids(poids);
		}
		
		articleRepository.save(art);
		return "redirect:/index";
	}
	
	@RequestMapping(value="/editArticle/{articleId:[0-9]+}", method=RequestMethod.GET)
	public ModelAndView editArticle(@PathVariable("articleId") int articleId) {
		Article a = articleRepository.findOne(articleId);
		if (a == null) return new ModelAndView("redirect:/index");
		ModelAndView mv = new ModelAndView("liste");
		mv.addObject("id", a.getId());
		mv.addObject("nom", a.getNom());
		mv.addObject("description", a.getDescription());
		mv.addObject("prix", a.getPrix());
		mv.addObject("poids", a.getPoids());
		mv.addObject("articles", articleRepository.findAll());
		return mv;
	}
	
	
	@RequestMapping(value="/deleteArticle/{articleId:[0-9]+}", method=RequestMethod.POST)
	public String deleteArticle(@PathVariable("articleId") int articleId) {
		articleRepository.delete(articleId);
		return "redirect:/index";
	}
	
	

}
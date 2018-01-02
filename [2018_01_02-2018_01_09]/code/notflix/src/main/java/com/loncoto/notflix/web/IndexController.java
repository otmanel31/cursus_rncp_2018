package com.loncoto.notflix.web;

import java.util.Date;
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

import com.loncoto.notflix.metier.Film;
import com.loncoto.notflix.repositories.FilmDepot;

@Controller
@RequestMapping(value="/")
public class IndexController {

	@Autowired
	private FilmDepot filmDepot;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectToIndex() {
		return "redirect:/films";
	}

	@RequestMapping(value= "/films", method = RequestMethod.GET)
	public ModelAndView liste() {
		ModelAndView model = new ModelAndView("films");
		model.addObject("films", filmDepot.findAll());
		
		return model;
	}
	
	@RequestMapping(value= "/addFilm", method = RequestMethod.POST)
	public String addFilm(@RequestParam("titre") String titre,
							@RequestParam("realisateur") String realisateur,
							@RequestParam(value="annee", defaultValue="1999") int annee,
							@RequestParam("synopsis")String synopsis) {
		filmDepot.save(new Film(0, titre, realisateur, annee, synopsis));
		return "redirect:/films";
	}
	
	@RequestMapping(value= "/deleteFilm/{filmId:[0-9]+}", method = RequestMethod.POST)
	public String deleteFilm(@PathVariable("filmId") int filmId) {
		filmDepot.delete(filmId);
		return "redirect:/films";
	}
	
}
package com.loncoto.mangamaniaForm.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import com.loncoto.mangamaniaForm.metier.Manga;
import com.loncoto.mangamaniaForm.repositories.MangaRepository;

@Controller
@RequestMapping(value="/")
public class IndexController {

	@Autowired
	private MangaRepository mangaRepository;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectToIndex() {
		return "home";
	}

	@RequestMapping(value="/mangas",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Manga> listeManga() {
		ArrayList<Manga> data = new ArrayList<>();
		mangaRepository.findAll().forEach(data::add);
		return data;
	}
	
	@RequestMapping(value="/pmangas",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Page<Manga> listePManga(@PageableDefault(page=0, size=5) Pageable page) {
		return mangaRepository.findAll(page);
	}
	
	@RequestMapping(value="/pmangas/search/{search:.+}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Page<Manga> searchPManga(@PathVariable("search") String search,
									@PageableDefault(page=0, size=5) Pageable page) {
		return mangaRepository.findByTitreContaining(search, page);
	}
	
	
	
	@RequestMapping(value="/mangas/search/{search:.+}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Manga> searchManga(@PathVariable("search") String search) {
		return mangaRepository.findByTitreContaining(search);
	}
	
	@RequestMapping(value="/mangas",
			method=RequestMethod.POST,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Manga saveManga(@RequestBody Manga manga) {
		return mangaRepository.save(manga);
	}
	
	@RequestMapping(value="/mangas",
			method=RequestMethod.PUT,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Manga updateManga(@RequestBody Manga manga) {
		Manga m = mangaRepository.findOne(manga.getId());
		if (m == null) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "manga inconnu");
		}
		return mangaRepository.save(manga);
	}
	
	@RequestMapping(value="/mangas/{id:[0-9]+}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Manga findManga(@PathVariable("id") int id) {
		Manga m = mangaRepository.findOne(id);
		if (m == null)
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "manga inconnu");
		return m;
	}

	
	@RequestMapping(value="/mangas/{id:[0-9]+}",
			method=RequestMethod.DELETE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Manga deleteManga(@PathVariable("id") int id) {
		Manga m = mangaRepository.findOne(id);
		if (m == null)
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "manga inconnu");
		mangaRepository.delete(m);
		return m;
	}

	
}
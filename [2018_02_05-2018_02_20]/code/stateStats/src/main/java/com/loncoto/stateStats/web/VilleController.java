package com.loncoto.stateStats.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.loncoto.stateStats.metier.Villes;
import com.loncoto.stateStats.repositories.VillesRepository;

@Controller
@RequestMapping("extendedapi/villes")
public class VilleController {

	@Autowired
	private VillesRepository villesRepository;
	
	@RequestMapping(value="/searchByPop/{popMin:[0-9]+}",
					produces= { MediaType.APPLICATION_JSON_VALUE},
					method=RequestMethod.POST)
	@ResponseBody
	public Page<Villes> findWithPop(@PathVariable("popMin") int popMin,
									@PageableDefault(page=0, size=10) Pageable page) {
		return villesRepository.findByPopGreaterThan(popMin, page);
	}
	
	
}

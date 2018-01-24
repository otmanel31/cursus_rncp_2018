package com.loncoto.planificatorform.web;

import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.loncoto.planificatorform.metier.Intervention;
import com.loncoto.planificatorform.repositories.IntervenantRepository;
import com.loncoto.planificatorform.repositories.InterventionRepository;

@Controller
@RequestMapping(value="/")
public class IndexController {

	@Autowired
	private InterventionRepository interventionRepository;
	@Autowired
	private IntervenantRepository intervenantRepository;
	
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectToIndex() {
		return "redirect:/interventions";
	}

	
	@RequestMapping(value = "/interventions",
					method = RequestMethod.GET,
					produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Page<Intervention> printWelcome(@PageableDefault(page=0, size=10) Pageable page) {
		return interventionRepository.findAll(page);
	}



}
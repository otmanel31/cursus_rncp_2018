package com.loncoto.firstsecurityform.web;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.loncoto.firstsecurityform.metier.Role;
import com.loncoto.firstsecurityform.metier.Utilisateur;
import com.loncoto.firstsecurityform.repositories.IInternalRepository;
import com.loncoto.firstsecurityform.repositories.InternalRepository;
import com.loncoto.firstsecurityform.repositories.RoleRepository;
import com.loncoto.firstsecurityform.repositories.UtilisateurRepository;

@Controller
@RequestMapping("/")
public class IndexController {
	
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private IInternalRepository InternalRepository;
	
	
	@Autowired
	private PasswordEncoder myPasswordEncoder;
	
	@RequestMapping(value="/", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> index () {
		Map<String, Object> result = new HashMap<>();
		result.put("message", "vous etes sur la page d'acceuil");
		result.put("date", new Date());
		return result;
	}
	
	@RequestMapping(value="/public", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> indexpublic() {
		Map<String, Object> result = new HashMap<>();
		result.put("message", "vous etes sur la page public");
		result.put("date", new Date());
		// autre manière d'accéder a l'utilisateur authentifié
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		result.put("utilisateur", auth.getName());
		result.put("autorities", auth.getAuthorities().stream()
											   .map(au -> au.getAuthority())
											   .collect(Collectors.toList()));
		return result;
	}
	
	@RequestMapping(value="/client", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> indexclient(Principal principal) {
		Map<String, Object> result = new HashMap<>();
		result.put("message", "vous etes sur la page client");
		result.put("date", new Date());
		// information sur l'utilisateur
		result.put("utilisateur", principal.getName());
		return result;
	}
	
	@RequestMapping(value="/admin", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> indexadmin(Authentication authentication) {
		Map<String, Object> result = new HashMap<>();
		result.put("message", "vous etes sur la page admin");
		result.put("date", new Date());
		// information sur l'utilisateur
		result.put("utilisateur", authentication.getName());
		result.put("autorities", authentication.getAuthorities().stream()
											   .map(au -> au.getAuthority())
											   .collect(Collectors.toList()));
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_VISITOR')")
	@RequestMapping(value="/toto", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> toto() {
		Map<String, Object> result = new HashMap<>();
		result.put("message", "vous etes sur la page toto");
		result.put("date", new Date());
		return result;
	}
	
	@RequestMapping(value="/register", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Utilisateur register(@RequestParam("username") String username,
								@RequestParam("password") String password) {
		//Utilisateur u = new Utilisateur(0, username, myPasswordEncoder.encode(password), true);
		Role r = roleRepository.findByRoleName("ROLE_USER");
		Utilisateur u = InternalRepository
								.createUser(username, myPasswordEncoder.encode(password), r);
		return u;
	}
	
}

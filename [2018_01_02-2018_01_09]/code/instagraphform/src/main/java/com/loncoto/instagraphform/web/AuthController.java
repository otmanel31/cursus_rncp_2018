package com.loncoto.instagraphform.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

import com.loncoto.instagraphform.metier.Role;
import com.loncoto.instagraphform.metier.Utilisateur;
import com.loncoto.instagraphform.repositories.RoleRepository;
import com.loncoto.instagraphform.repositories.UtilisateurRepository;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/extendedapi/auth")
@Log4j
public class AuthController {
	
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder myPasswordEncoder;
	
	@CrossOrigin(origins= "http://localhost:4200")
	@RequestMapping(value="/login",
					method=RequestMethod.POST,
					produces= MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Utilisateur login(@RequestBody Utilisateur utilisateur) {
		log.info("login asked with " + utilisateur.getUsername());
		Utilisateur u = utilisateurRepository.findByUsername(utilisateur.getUsername());
		if (u != null)
			return u;
		else
			throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "login failed");
	}
	
	@CrossOrigin(origins= "http://localhost:4200")
	@RequestMapping(value="/register",
					method=RequestMethod.POST,
					produces= MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@PreAuthorize("permitAll")
	public Utilisateur register(@RequestParam("username") String username,
								@RequestParam("password") String password) {
		log.info("register asked for " + username);
		Utilisateur u = new Utilisateur(0, username, myPasswordEncoder.encode(password), true);
		Role r = roleRepository.findByRoleName("ROLE_USER");
		u.getRoles().add(r);
		u = utilisateurRepository.save(u);
		return u;
	}
	
	
	
}

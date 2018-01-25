package com.loncoto.instagraphform.config;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.loncoto.instagraphform.metier.Role;
import com.loncoto.instagraphform.metier.Utilisateur;
import com.loncoto.instagraphform.repositories.RoleRepository;
import com.loncoto.instagraphform.repositories.UtilisateurRepository;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class DatabaseContentInitialiser implements ApplicationListener<ContextRefreshedEvent> {

	//private static Logger log = LogManager.getLogger(DatabaseContentInitialiser.class);
	
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		log.info("nothing to do...");	
	}

}

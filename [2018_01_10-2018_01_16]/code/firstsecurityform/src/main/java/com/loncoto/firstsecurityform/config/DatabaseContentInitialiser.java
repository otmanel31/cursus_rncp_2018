package com.loncoto.firstsecurityform.config;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.loncoto.firstsecurityform.metier.Role;
import com.loncoto.firstsecurityform.metier.Utilisateur;
import com.loncoto.firstsecurityform.repositories.IInternalRepository;

@Service
public class DatabaseContentInitialiser implements ApplicationListener<ContextRefreshedEvent>
{
	private static Logger log = LogManager.getLogger(DatabaseContentInitialiser.class); 
	
	@Autowired
	private IInternalRepository internalRepository;
	
	@Autowired
	private PasswordEncoder myPasswordEncoder;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		if (internalRepository.countUsers() == 0) {
			// pas d'utilisateur dans la base, probablement base vide
			// on creer des utilisateurs par défaut et leur roles
			log.info("base seem empty, initialising default content...");
			Role r_admin = internalRepository.createRole("ROLE_ADMIN");
			Role r_user = internalRepository.createRole("ROLE_USER");
			Role r_visitor = internalRepository.createRole("ROLE_VISITOR");
			
			Utilisateur u_admin = internalRepository.createUser("admin",
																myPasswordEncoder.encode("admin"),
																r_admin, r_user);
			Utilisateur u_vincent = internalRepository.createUser("vincent",
					myPasswordEncoder.encode("1234"),
					r_user);
			Utilisateur u_elon = internalRepository.createUser("elon",
					myPasswordEncoder.encode("marslove"),
					r_visitor);
		}
		else
			log.info("base isn't empty, no initialisation required");
		
	}

}

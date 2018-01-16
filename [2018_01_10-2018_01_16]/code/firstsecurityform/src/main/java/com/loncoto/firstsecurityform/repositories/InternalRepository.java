package com.loncoto.firstsecurityform.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loncoto.firstsecurityform.metier.Role;
import com.loncoto.firstsecurityform.metier.Utilisateur;

@Service
public class InternalRepository implements IInternalRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional(readOnly=true)
	public long countUsers() {
		return em.createQuery("select count(u) from Utilisateur as u", Long.class)
				 .getSingleResult();
	}
	
	@Override
	@Transactional
	public Role createRole(String roleName) {
		Role r = new Role(0, roleName);
		em.persist(r);
		return r;
	}
	
	@Override
	@Transactional
	public Utilisateur createUser(String userName, String password, Role...roles) {
		Utilisateur u = new Utilisateur(0, userName, password, true);
		for (Role r : roles) {
			u.getRoles().add(r);
		}
		em.persist(u);
		return u;
	}
}

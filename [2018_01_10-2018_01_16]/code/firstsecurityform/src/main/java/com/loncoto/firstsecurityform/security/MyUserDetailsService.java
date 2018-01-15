package com.loncoto.firstsecurityform.security;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loncoto.firstsecurityform.metier.Utilisateur;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		TypedQuery<Utilisateur> q =
				em.createQuery("select u from Utilisateur as u left join fetch u.roles"
								+ "	where u.username = :username",
								Utilisateur.class);
		q.setParameter("username", username);
		Utilisateur u = q.getSingleResult();
		if (u == null)
			throw new UsernameNotFoundException("utilisateur inconnu");
		
		return new MyUserDetails(u);
	}

}

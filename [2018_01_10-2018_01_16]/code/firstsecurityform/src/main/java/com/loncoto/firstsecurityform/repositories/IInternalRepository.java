package com.loncoto.firstsecurityform.repositories;

import org.springframework.transaction.annotation.Transactional;

import com.loncoto.firstsecurityform.metier.Role;
import com.loncoto.firstsecurityform.metier.Utilisateur;

public interface IInternalRepository {

	long countUsers();

	Role createRole(String roleName);

	Utilisateur createUser(String userName, String password, Role... roles);

}
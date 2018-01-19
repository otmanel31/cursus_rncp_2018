package com.loncoto.instagraphform.repositories;

import org.springframework.data.repository.CrudRepository;

import com.loncoto.instagraphform.metier.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
	
	Role findByRoleName(String roleName);
}

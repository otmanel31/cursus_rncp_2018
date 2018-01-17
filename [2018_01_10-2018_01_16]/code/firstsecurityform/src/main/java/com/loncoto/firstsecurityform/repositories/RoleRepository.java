package com.loncoto.firstsecurityform.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import com.loncoto.firstsecurityform.metier.Role;

@RepositoryRestResource
@PreAuthorize("hasRole('ROLE_USER')")
public interface RoleRepository extends PagingAndSortingRepository<Role, Integer> {

	@PreAuthorize("permitAll")
	Role findByRoleName(String roleName);
}

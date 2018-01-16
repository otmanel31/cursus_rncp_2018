package com.loncoto.firstsecurityform.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import com.loncoto.firstsecurityform.metier.Role;

@PreAuthorize("hasRole('ROLE_USER')")
public interface RoleRepository extends PagingAndSortingRepository<Role, Integer> {

}

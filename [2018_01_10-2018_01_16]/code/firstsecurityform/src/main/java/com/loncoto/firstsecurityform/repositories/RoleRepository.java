package com.loncoto.firstsecurityform.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.loncoto.firstsecurityform.metier.Role;

public interface RoleRepository extends PagingAndSortingRepository<Role, Integer> {

}

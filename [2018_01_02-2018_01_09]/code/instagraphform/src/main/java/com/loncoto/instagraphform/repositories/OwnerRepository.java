package com.loncoto.instagraphform.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import com.loncoto.instagraphform.metier.CompositeOwnerKey;
import com.loncoto.instagraphform.metier.Owner;

@PreAuthorize("permitAll")
public interface OwnerRepository extends CrudRepository<Owner, CompositeOwnerKey> {

}

package com.loncoto.firstrBoot.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.loncoto.firstrBoot.metier.SpaceShip;

public interface SpaceShipRepository extends PagingAndSortingRepository<SpaceShip, Integer> {

}

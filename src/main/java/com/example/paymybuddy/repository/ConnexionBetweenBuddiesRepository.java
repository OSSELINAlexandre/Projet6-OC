package com.example.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.paymybuddy.model.ConnexionBetweenBuddies;

@Repository
public interface ConnexionBetweenBuddiesRepository extends CrudRepository<ConnexionBetweenBuddies, Integer> {

	
	ConnexionBetweenBuddies findByIdOfCenterAndBuddyOfACenter(int idOfCenter, int buddyOfACenter);
}

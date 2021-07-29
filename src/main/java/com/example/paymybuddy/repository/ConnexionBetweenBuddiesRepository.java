package com.example.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.paymybuddy.model.ConnectionBetweenBuddies;

@Repository
public interface ConnexionBetweenBuddiesRepository extends CrudRepository<ConnectionBetweenBuddies, Integer> {

	
	ConnectionBetweenBuddies findByIdOfCenterAndBuddyOfACenter(int idOfCenter, int buddyOfACenter);
}

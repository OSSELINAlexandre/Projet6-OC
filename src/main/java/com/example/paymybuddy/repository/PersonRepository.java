package com.example.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.paymybuddy.model.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {

	Person findByEmail(String email);
	
	Person findByEmailAndPassword(String email, String password);

}

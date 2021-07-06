package com.example.premierbrouillonapplication.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.premierbrouillonapplication.model.IdentificationData;
import com.example.premierbrouillonapplication.model.Person;
import com.example.premierbrouillonapplication.repository.BankAccountRepository;
import com.example.premierbrouillonapplication.repository.PersonRepository;

@Service
public class MyAppServices {

	@Autowired
	BankAccountRepository bankRepo;

	@Autowired
	PersonRepository personRepo;

	public void saveNewPerson(Person p) {
		personRepo.save(p);
	}
	
	public Person findById(IdentificationData searchedPerson) {
		
		Iterable<Person> allList = personRepo.findAll();
		
		for(Person p : allList) {
			
			if(p.geteMail().equals(searchedPerson.getEmail()) && ( p.getPassword().equals(searchedPerson.getPassword()))) {
				
				return p;
			}
		}
		
		return null;
		
	}

}

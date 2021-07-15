package com.example.paymybuddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.paymybuddy.DTO.IdentificationData;
import com.example.paymybuddy.DTO.LoginRegistration;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.repository.PersonRepository;

@Service
public class UserServices {

	@Autowired
	PersonRepository personRepo;

	public UserServices() {
	}

	public void saveANewPersonInTheDB(LoginRegistration person) {

		Person newItem = new Person();
		newItem.seteMail(person.geteMail());
		newItem.setLastName(person.getLastName());
		newItem.setName(person.getName());
		newItem.setPassword(person.getPassword());
		newItem.setAmount(0.0);

		personRepo.save(newItem);

	}

	public boolean checkIfTheEmailExistsInTheDB(LoginRegistration person) {

		if (personRepo.findByEmail(person.geteMail()) != null) {

			return true;

		} else {

			return false;
		}

	}

	public Person findByIdentificationDataIfCombinasioExists(IdentificationData person) {

		return personRepo.findByEmailAndPassword(person.getEmail(), person.getPassword());

	}

}

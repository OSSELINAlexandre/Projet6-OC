package com.example.premierbrouillonapplication.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.premierbrouillonapplication.model.BuddiesInConnexion;
import com.example.premierbrouillonapplication.model.LoginRegistration;
import com.example.premierbrouillonapplication.model.Person;
import com.example.premierbrouillonapplication.repository.PersonRepository;

@Service
public class PersonServices {

	@Autowired
	PersonRepository personRepo;

	public PersonServices() {
		super();
	}

	public void saveIt(Person P) {

		personRepo.save(P);
	}

	public Person getIt(Integer p) {

		for (Person aperson : personRepo.findAll()) {

			if (aperson.getId() == p)
				return aperson;

		}
		return null;
	}

	public Iterable<Person> getItAll() {
		return personRepo.findAll();
	}

	public Map<Integer, String> checkEmailFromBuddy(BuddiesInConnexion bud) {

		Map<Integer, String> result = new HashMap<>();

		for (Person p : personRepo.findAll()) {

			if (p.geteMail().equals(bud.getEmail())) {

				result.put(p.getId(), p.getLastName() + ", " + p.getName());

				return result;

			}

		}

		return result;
	}

	public void saveNewPerson(LoginRegistration person) {

		
		Person newItem = new Person();
		newItem.seteMail(person.geteMail());
		newItem.setLastName(person.getLastName());
		newItem.setName(person.getName());
		newItem.setPassword(person.getPassword());

		personRepo.save(newItem);

	}

}

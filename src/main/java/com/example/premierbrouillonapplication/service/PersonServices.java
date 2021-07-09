package com.example.premierbrouillonapplication.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.premierbrouillonapplication.DTO.BuddiesInConnexion;
import com.example.premierbrouillonapplication.DTO.IdentificationData;
import com.example.premierbrouillonapplication.DTO.LoginRegistration;
import com.example.premierbrouillonapplication.model.Person;
import com.example.premierbrouillonapplication.repository.PersonRepository;

@Service
public class PersonServices {

	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PersonServices.class);

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

	public Map<Integer, String> checkEmailFromBuddy(BuddiesInConnexion bud, Person currentUser) {

		Map<Integer, String> result = new HashMap<>();

		for (Person p : personRepo.findAll()) {

			if (p.geteMail().equals(bud.getEmail()) && !p.geteMail().equals(currentUser.geteMail())) {

				result.put(p.getId(), p.getLastName() + ", " + p.getName());

				return result;

			} else if (p.geteMail().equals(bud.getEmail()) && p.geteMail().equals(currentUser.geteMail())) {

				result.put(0, "BUG");
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

	public boolean checkExistingMail(LoginRegistration person) {

		for (Person p : personRepo.findAll()) {

			if (p.geteMail().equals(person.geteMail())) {

				return true;
			}

		}

		return false;
	}

	public Person findById(IdentificationData person) {

		Iterable<Person> allList = personRepo.findAll();

		for (Person p : allList) {

			if (p.geteMail().equals(person.getEmail()) && (p.getPassword().equals(person.getPassword()))) {

				return p;
			}
		}

		return null;
	}

}

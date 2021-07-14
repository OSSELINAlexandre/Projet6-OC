package com.example.paymybuddy.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.paymybuddy.DTO.BuddiesInConnexion;
import com.example.paymybuddy.DTO.IdentificationData;
import com.example.paymybuddy.DTO.LoginRegistration;
import com.example.paymybuddy.model.BankOperation;
import com.example.paymybuddy.model.ConnexionBetweenBuddies;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.model.Transaction;
import com.example.paymybuddy.repository.BankOperationRepository;
import com.example.paymybuddy.repository.ConnexionBetweenBuddiesRepository;
import com.example.paymybuddy.repository.PersonRepository;

@Service
public class UserServices {

	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(UserServices.class);

	@Autowired
	PersonRepository personRepo;

	@Autowired
	BankOperationRepository operationRepo;

	@Autowired
	ConnexionBetweenBuddiesRepository connexionRepo;

	public UserServices() {
	}

	public void saveIt(Person P) {

		personRepo.save(P);
	}

	public Person getIt(Integer p) {

		return personRepo.findById(p).get();
	}

	public Iterable<Person> getItAll() {
		return personRepo.findAll();
	}

	public void saveNewPerson(LoginRegistration person) {

		Person newItem = new Person();
		newItem.seteMail(person.geteMail());
		newItem.setLastName(person.getLastName());
		newItem.setName(person.getName());
		newItem.setPassword(person.getPassword());
		newItem.setAmount(0.0);

		personRepo.save(newItem);

	}

	public boolean checkExistingMail(LoginRegistration person) {

		if (personRepo.findByEmail(person.geteMail()) != null) {

			return true;

		} else {

			return false;
		}

	}

	public Double getTheAccountBalance(int id) {

		return personRepo.findById(id).get().getAmount();

	}

	public Person findByIdentificationDataLogin(IdentificationData person) {

		return personRepo.findByEmailAndPassword(person.getEmail(), person.getPassword());

	}

	public List<BankOperation> getAllOperations(Person currentUser) {

		return (List<BankOperation>) operationRepo.findByholder(currentUser);

	}

	public Map<Person, String> getListOfBuddiesForThymeleaf(Person currentUser) {

		Map<Person, String> result = new HashMap<>();

		for (ConnexionBetweenBuddies ba : currentUser.getListOfBuddies()) {

			Person buddy = personRepo.findById(ba.getBuddyOfACenter()).get();
			result.put(buddy, buddy.getName() + ", " + buddy.getLastName());
		}

		return result;
	}
}

package com.example.premierbrouillonapplication.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.premierbrouillonapplication.controller.MainController;
import com.example.premierbrouillonapplication.model.IdentificationData;
import com.example.premierbrouillonapplication.model.Person;
import com.example.premierbrouillonapplication.repository.BankAccountRepository;
import com.example.premierbrouillonapplication.repository.PersonRepository;

@Service
public class MyAppServices {

	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(MyAppServices.class);

	@Autowired
	BankAccountRepository bankRepo;

	@Autowired
	PersonRepository personRepo;

	public void saveNewPerson(Person p) {
		personRepo.save(p);
	}

	public Person findById(IdentificationData searchedPerson) {

		logger.info("GOING HERE NOW ? ");

		Optional<Person> pa = personRepo.findById(1);

		logger.info("fULL BUG IN SQL ?  " + pa.toString());
		Iterable<Person> allList = personRepo.findAll();

		logger.info("And not here ?");
		for (Person p : allList) {

			if (p.geteMail().equals(searchedPerson.getEmail())
					&& (p.getPassword().equals(searchedPerson.getPassword()))) {

				return p;
			}
		}

		return null;

	}

}

package com.example.paymybuddy.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.paymybuddy.DTO.BuddiesInConnexion;
import com.example.paymybuddy.DTO.IdentificationData;
import com.example.paymybuddy.DTO.LoginRegistration;
import com.example.paymybuddy.model.BankOperation;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.model.Transaction;
import com.example.paymybuddy.repository.BankOperationRepository;
import com.example.paymybuddy.repository.PersonRepository;

@Service
public class UserServices {

	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(UserServices.class);

	@Autowired
	PersonRepository personRepo;
	
	@Autowired
	BankOperationRepository operationRepo;

	public UserServices() {
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
		newItem.setAmount(0.0);

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

	public Double getTheAccountBalance(int id) {

		for (Person ba : personRepo.findAll()) {

			if (ba.getId() == id) {

				return ba.getAmount();
			}
		}

		return null;
	}

	public Person findById(IdentificationData person) {

		for (Person p : personRepo.findAll()) {

			if (p.geteMail().equals(person.getEmail()) && (p.getPassword().equals(person.getPassword()))) {

				return p;
			}
		}

		return null;
	}

	public Map<Integer, String> getListNoDuplicates(List<Transaction> listofAllTransaction, Person currentUser) {

		Map<Integer, String> result = new HashMap<>();
		Boolean flag = true;

		for (Transaction t : listofAllTransaction) {

			if (t.getPayee().getId() != currentUser.getId()) {

				if (!result.isEmpty()) {

					for (Integer Tr : result.keySet()) {

						if (Tr == t.getPayee().getId()) {

							flag = false;
						}

					}

					if (flag) {

						result.put(t.getPayee().getId(), t.getPayee().getLastName() + ", " + t.getPayee().getName());
					}

					flag = true;
				} else {

					result.put(t.getPayee().getId(), t.getPayee().getLastName() + ", " + t.getPayee().getName());
				}

			}
		}

		return result;
	}

	public List<BankOperation> getAllOperations(Person currentUser) {
		
		
		List<BankOperation> result = new ArrayList<BankOperation>();
		
		
		for(BankOperation bo : operationRepo.findAll()) {
			
			if(bo.getHolder().getId() == currentUser.getId()) {
				
				result.add(bo);
			}
		}
		
		
		return result;
	}

}

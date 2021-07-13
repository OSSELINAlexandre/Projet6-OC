package com.example.paymybuddy.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.paymybuddy.DTO.BuddiesInConnexion;
import com.example.paymybuddy.DTO.PaymentData;
import com.example.paymybuddy.constant.Fees;
import com.example.paymybuddy.model.BankOperation;
import com.example.paymybuddy.model.ConnexionBetweenBuddies;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.model.Transaction;
import com.example.paymybuddy.repository.BankOperationRepository;
import com.example.paymybuddy.repository.ConnexionBetweenBuddiesRepository;
import com.example.paymybuddy.repository.PersonRepository;
import com.example.paymybuddy.repository.TransactionRepository;

@Service
public class TransactionsServices {

	@Autowired
	TransactionRepository transacRepo;

	@Autowired
	PersonRepository personRepo;

	@Autowired
	BankOperationRepository bankAccountRepo;

	@Autowired
	ConnexionBetweenBuddiesRepository connexionRepo;

	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TransactionsServices.class);

	public TransactionsServices() {
	}

	public void saveIt(Transaction t) {

		transacRepo.save(t);
	}

	public Transaction getIt(int id) {

		for (Transaction t : transacRepo.findAll()) {

			if (t.getId() == id) {

				return t;
			}

		}

		return null;
	}

	public boolean checkAmounts(Person currentUser, double d) {

		for (Person person : personRepo.findAll()) {

			if (person.getId() == currentUser.getId()) {

				if (person.getAmount() - d >= 0) {

					return true;

				} else {

					return false;
				}

			}
		}

		return false;
	}

	public BankOperation findBankOperationById(int id) {

		for (BankOperation ba : bankAccountRepo.findAll()) {

			if (ba.getHolder().getId() == id) {

				return ba;
			}
		}

		return null;

	}

	public Transaction adjustAccount(PaymentData Data, Person currentUser) {

		Person theBud = personRepo.findById(Integer.parseInt(Data.getPersonToPay())).get();
		Double currentUserNewAmount = currentUser.getAmount() - (Data.getAmount() * (1 + Fees.CLASSIC_FEE_APP));
		Double buddyPaidNewAmount = theBud.getAmount() + Data.getAmount();
		currentUser.setAmount(currentUserNewAmount);
		theBud.setAmount(buddyPaidNewAmount);
		logger.info("IN ADJUSTACCOUNT, class BankAccountService, newUserAmount : " + currentUserNewAmount);
		logger.info("IN ADJUSTACCOUNT, class BankAccountService, newgonnaBePaidAccount : " + buddyPaidNewAmount);
	
		Transaction tempTransac = saveANewTransaction(currentUser, Data, theBud);
		personRepo.save(currentUser);
		personRepo.save(theBud);
		
		return tempTransac;

	}

	public Person getPersonById(Integer p) {

		for (Person aperson : personRepo.findAll()) {

			if (aperson.getId() == p)
				return aperson;

		}
		return null;
	}

	@Transactional
	public Transaction saveANewTransaction(Person currentUser, PaymentData pay, Person it) {

		Transaction newItem = new Transaction();
		newItem.setAmount(pay.getAmount());
		newItem.setCommentaire(pay.getDescription());
		newItem.setPayee(it);
		newItem.setPayeur(currentUser);

		transacRepo.save(newItem);
		return newItem;
	}

	public ConnexionBetweenBuddies addingABuddyToTheCurrentUser(BuddiesInConnexion bud, Person currentUser) {

		Boolean result = true;
		Person buddyInConnection = personRepo.findByEmail(bud.getEmail());

		for (ConnexionBetweenBuddies budi : connexionRepo.findAll()) {

			if (budi.getBuddyOfACenter() == buddyInConnection.getId() && budi.getIdOfCenter() == currentUser.getId()) {

				result = false;
			}

		}

		if (result) {

			ConnexionBetweenBuddies newConnexion = new ConnexionBetweenBuddies();
			newConnexion.setIdOfCenter(currentUser.getId());
			newConnexion.setBuddyOfACenter(buddyInConnection.getId());
			connexionRepo.save(newConnexion);
			
			return newConnexion;

		}
		return null;

	}

	public boolean checkIfBuddyExists(BuddiesInConnexion bud) {

		for (Person p : personRepo.findAll()) {

			if (p.geteMail().equals(bud.getEmail())) {

				return true;
			}

		}

		return false;
	}

	public Map<Person, String> addTemporaryConnexion(ConnexionBetweenBuddies temporaryConnexion) {
		
		Map<Person, String> temporaryResult = new HashMap<>();
		
		Person tempBuddy = personRepo.findById(temporaryConnexion.getBuddyOfACenter()).get();
		
		temporaryResult.put(tempBuddy, tempBuddy.getName() + ", " + tempBuddy.getLastName());

		return temporaryResult;
		
	}



}

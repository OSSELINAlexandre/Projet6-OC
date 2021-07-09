package com.example.paymybuddy.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.paymybuddy.DTO.BuddiesInConnexion;
import com.example.paymybuddy.DTO.PaymentData;
import com.example.paymybuddy.constant.Fees;
import com.example.paymybuddy.model.BankOperation;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.model.Transaction;
import com.example.paymybuddy.repository.BankOperationRepository;
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

	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TransactionsServices.class);

	public TransactionsServices() {
		super();
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

	public void adjustAccount(Person buddyGoingToBePaidAccount, Person currentUser, Double amount) {


		Double currentUserNewAmount = currentUser.getAmount() - (amount * (1 + Fees.CLASSIC_FEE_APP));
		Double buddyPaidNewAmount = buddyGoingToBePaidAccount.getAmount() + amount;

		currentUser.setAmount(currentUserNewAmount);
		buddyGoingToBePaidAccount.setAmount(buddyPaidNewAmount);

		logger.info("IN ADJUSTACCOUNT, class BankAccountService, newUserAmount : " + currentUserNewAmount);
		logger.info("IN ADJUSTACCOUNT, class BankAccountService, newgonnaBePaidAccount : " + buddyPaidNewAmount);

		personRepo.save(currentUser);
		personRepo.save(buddyGoingToBePaidAccount);

	}

	public Person getPersonById(Integer p) {

		for (Person aperson : personRepo.findAll()) {

			if (aperson.getId() == p)
				return aperson;

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

}

package com.example.premierbrouillonapplication.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.premierbrouillonapplication.DTO.PaymentData;
import com.example.premierbrouillonapplication.model.Person;
import com.example.premierbrouillonapplication.model.Transaction;
import com.example.premierbrouillonapplication.repository.TransactionRepository;

@Service
public class TransactionsServices {

	@Autowired
	TransactionRepository transacRepo;

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
	public void saveANewTransaction(Person currentUser, PaymentData pay, Person it) {

		Transaction newItem = new Transaction();
		newItem.setAmount(pay.getAmount());
		newItem.setCommentaire(pay.getDescription());
		newItem.setPayee(it);
		newItem.setPayeur(currentUser);

		transacRepo.save(newItem);

	}

}

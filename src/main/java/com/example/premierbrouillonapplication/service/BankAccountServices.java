package com.example.premierbrouillonapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.premierbrouillonapplication.model.BankAccount;
import com.example.premierbrouillonapplication.model.PaymentData;
import com.example.premierbrouillonapplication.model.Person;
import com.example.premierbrouillonapplication.repository.BankAccountRepository;
import com.example.premierbrouillonapplication.repository.PersonRepository;

@Service
public class BankAccountServices {

	@Autowired
	BankAccountRepository bankAccountRepo;

	public BankAccountServices() {
		super();
	}

	public void saveIt(BankAccount B) {

		bankAccountRepo.save(B);
	}

	public BankAccount findById(int id) {

		for (BankAccount ba : bankAccountRepo.findAll()) {

			if (ba.getHolder().getId() == id) {

				return ba;
			}
		}

		return null;

	}

	public Double getTheAccountBalance(int id) {

		for (BankAccount ba : bankAccountRepo.findAll()) {

			if (ba.getHolder().getId() == id) {

				return ba.getAmount();
			}
		}

		return null;
	}

	public boolean checkAvailability(Person it, Person currentUser, Integer amount) {

		BankAccount userAccount = findById(currentUser.getId());
		
		if(userAccount.getAmount() >= amount) {
			return true;
		}else {
		return false;
		}
	}

	public void adjustAccount(Person it, Person currentUser, Integer amount) {
		
		BankAccount userAccount = findById(currentUser.getId());
		BankAccount gonnaBePaidAccount = findById(it.getId());
		
		
		Double newUserAmount = userAccount.getAmount() - amount;
		Double newgonnaBePaidAccount = gonnaBePaidAccount.getAmount() + amount;
		
		userAccount.setAmount(newUserAmount);
		gonnaBePaidAccount.setAmount(newgonnaBePaidAccount);
		
		
		bankAccountRepo.save(userAccount);
		bankAccountRepo.save(gonnaBePaidAccount);

	}




}

package com.example.premierbrouillonapplication.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.premierbrouillonapplication.controller.MainController;
import com.example.premierbrouillonapplication.model.BankAccount;
import com.example.premierbrouillonapplication.model.BankAccountWithdrawalDepositInformation;
import com.example.premierbrouillonapplication.model.PaymentData;
import com.example.premierbrouillonapplication.model.Person;
import com.example.premierbrouillonapplication.repository.BankAccountRepository;
import com.example.premierbrouillonapplication.repository.PersonRepository;

@Service
public class BankAccountServices {

	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(BankAccountServices.class);

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

	public boolean checkAvailability(Person it, Person currentUser, Double amount) {

		BankAccount userAccount = findById(currentUser.getId());

		if (userAccount.getAmount() >= amount) {
			return true;
		} else {
			return false;
		}
	}

	public void adjustAccount(Person it, Person currentUser, Double amount) {

		BankAccount userAccount = findById(currentUser.getId());
		BankAccount gonnaBePaidAccount = findById(it.getId());

		Double newUserAmount = userAccount.getAmount() - (amount * 1.005);
		Double newgonnaBePaidAccount = gonnaBePaidAccount.getAmount() + amount;

		userAccount.setAmount(newUserAmount);
		gonnaBePaidAccount.setAmount(newgonnaBePaidAccount);

		logger.info("IN ADJUSTACCOUNT, class BankAccountService, newUserAmount : " + newUserAmount);
		logger.info("IN ADJUSTACCOUNT, class BankAccountService, newgonnaBePaidAccount : " + newgonnaBePaidAccount);

		bankAccountRepo.save(userAccount);
		bankAccountRepo.save(gonnaBePaidAccount);

	}

	public void saveForDepositorWithdrawal(Person currentUser, Double depositMoney) {

		for (BankAccount b : bankAccountRepo.findAll()) {

			if (b.getHolder().getId() == currentUser.getId()) {

				logger.info("-------------X-------------X-----------" + depositMoney);
				Double oldAmount = b.getAmount();
				
				Double newAmount = oldAmount + depositMoney ;
				
		        BigDecimal bd = new BigDecimal (newAmount).setScale(2, RoundingMode.HALF_UP);
		        double finalThink = bd.doubleValue ();
		        
				b.setAmount(finalThink);
				logger.info("-------------o-------------O-----------" + b.getAmount());
				bankAccountRepo.save(b);
				return;

			}

		}

		BankAccount ba = new BankAccount();
		ba.setAmount(depositMoney);
		ba.setHolder(currentUser);
		bankAccountRepo.save(ba);
		return;

	}

	public boolean checkAmounts(Person currentUser, double d) {

		for (BankAccount b : bankAccountRepo.findAll()) {

			if (b.getHolder().getId() == currentUser.getId()) {

				if (b.getAmount() - d >= 0) {

					return true;

				} else {

					return false;
				}

			}
		}

		return false;
	}

}

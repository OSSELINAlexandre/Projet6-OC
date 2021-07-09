package com.example.paymybuddy.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.paymybuddy.DTO.BankAccountWithdrawalDepositInformation;
import com.example.paymybuddy.DTO.PaymentData;
import com.example.paymybuddy.model.BankOperation;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.repository.BankOperationRepository;
import com.example.paymybuddy.repository.PersonRepository;

@Service
public class OperationOnAccountServices {

	private static final org.apache.logging.log4j.Logger logger = LogManager
			.getLogger(OperationOnAccountServices.class);

	@Autowired
	BankOperationRepository bankAccountRepo;

	@Autowired
	PersonRepository userServices;

	public OperationOnAccountServices() {
		super();
	}

	public void saveIt(BankOperation B) {

		bankAccountRepo.save(B);
	}

	public Person findById(int id) {

		for (Person ba : userServices.findAll()) {

			if (ba.getId() == id) {

				return ba;
			}
		}

		return null;

	}

	public void saveForDepositorWithdrawal(Person currentUser, Double depositMoney, boolean depositTrueWithdrawFalse) {

		BankOperation newBankOperation = new BankOperation();
		newBankOperation.setDepositIsTrueWithdrawIsFalse(depositTrueWithdrawFalse);
		newBankOperation.setHolder(currentUser);
		newBankOperation.setAmount(depositMoney);
		Double oldAmout = currentUser.getAmount();
		Double newAmount = 0.0;

		if (depositTrueWithdrawFalse) {

			newAmount = oldAmout + depositMoney;

		} else {

			newAmount = oldAmout - depositMoney;
		}

		BigDecimal bd = new BigDecimal(newAmount).setScale(2, RoundingMode.HALF_UP);
		double finalAmountSaved = bd.doubleValue();

		currentUser.setAmount(finalAmountSaved);
		userServices.save(currentUser);
		bankAccountRepo.save(newBankOperation);
		return;

	}

	public boolean checkAmounts(Person currentUser, double d) {

		for (Person person : userServices.findAll()) {

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

}

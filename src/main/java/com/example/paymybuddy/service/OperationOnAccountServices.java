package com.example.paymybuddy.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
	PersonRepository userRepo;

	public OperationOnAccountServices() {
	}

	public void saveForDepositorWithdrawal(Person currentUser, Double depositMoney, boolean depositTrueWithdrawFalse,
			HttpSession session) {

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
		userRepo.save(currentUser);
		bankAccountRepo.save(newBankOperation);

		List<BankOperation> theListInSession = (List<BankOperation>) session.getAttribute("listOfAllOperations");

		theListInSession.add(newBankOperation);
		session.setAttribute("listOfAllOperations", theListInSession);

	}

	public boolean checkIfCurrentUserHasNecessaryFunds(Person currentUser, double d) {

		if (currentUser.getAmount() - d >= 0) {

			return true;

		} else {

			return false;
		}

	}

	public boolean checkIfCurrentUserCanStillDepositToItsAccount(Person currentUser, double amount) {

		if (currentUser.getAmount() + amount < 9999999) {

			return true;

		} else {

			return false;
		}

	}
	
	//====== Getters and Setters of repository solely needed for testing purposes. 
	//====== Once the app is validated, and for security reasons, these getters and setters 
	//====== can be deleted
	
	

	public BankOperationRepository getBankAccountRepo() {
		return bankAccountRepo;
	}

	public void setBankAccountRepo(BankOperationRepository bankAccountRepo) {
		this.bankAccountRepo = bankAccountRepo;
	}

	public PersonRepository getUserRepo() {
		return userRepo;
	}

	public void setUserRepo(PersonRepository userRepo) {
		this.userRepo = userRepo;
	}
	

	
	
	

}

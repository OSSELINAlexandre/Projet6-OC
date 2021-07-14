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
	PersonRepository userServices;

	public OperationOnAccountServices() {
	}

	public void saveIt(BankOperation B) {

		bankAccountRepo.save(B);
	}

	public Person findById(int id) {

		return userServices.findById(id).get();

	}

	public BankOperation saveForDepositorWithdrawal(Person currentUser, Double depositMoney,
			boolean depositTrueWithdrawFalse) {

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
		return newBankOperation;

	}

	public boolean checkAmounts(Person currentUser, double d) {

		if (currentUser.getAmount() - d >= 0) {

			return true;

		} else {

			return false;
		}

	}

	public List<BankOperation> saveTemporaryListForBankOperation(Person currentUser, BankOperation transitoryItem,
			HttpSession session) {

		List<BankOperation> resultList = currentUser.getListOfALLOperations();
		resultList.add(transitoryItem);

		return resultList;
	}

	public void setTemporaryList(BankOperation transitoryItem, HttpSession session) {

		List<BankOperation> theResult = (List<BankOperation>) session.getAttribute("listOfAllOperations");
		theResult.add(transitoryItem);
		session.setAttribute("listOfAllOperations", theResult);

	}

}

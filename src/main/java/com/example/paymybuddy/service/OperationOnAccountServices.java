package com.example.paymybuddy.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.paymybuddy.dto.BankAccountWithdrawalDepositInformation;
import com.example.paymybuddy.dto.BankInformation;
import com.example.paymybuddy.model.BankOperation;
import com.example.paymybuddy.model.ExternalBankAccount;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.repository.BankOperationRepository;
import com.example.paymybuddy.repository.ExternalBankRepository;
import com.example.paymybuddy.repository.PersonRepository;

@Service
public class OperationOnAccountServices {

	private static final org.apache.logging.log4j.Logger logger = LogManager
			.getLogger(OperationOnAccountServices.class);

	@Autowired
	BankOperationRepository bankAccountRepository;

	@Autowired
	PersonRepository userRepository;

	@Autowired
	ExternalBankRepository externalBankRepository;

	public OperationOnAccountServices() {
	}

	@Transactional
	public void saveForDepositorWithdrawal(Person currentUser, BankAccountWithdrawalDepositInformation withdrawMoney, boolean depositTrueWithdrawFalse,
			HttpSession session) {

		BankOperation newBankOperation = new BankOperation();
		newBankOperation.setDepositIsTrueWithdrawIsFalse(depositTrueWithdrawFalse);
		newBankOperation.setHolder(currentUser);
		newBankOperation.setAmount(withdrawMoney.getAmount());
		ExternalBankAccount itemToSave = externalBankRepository.findById(Integer.parseInt(withdrawMoney.getBankAccountToDoOperationID())).get();
		
		
		
		logger.info("|||||C||||| TEST A in withdepot  " + itemToSave);
		
		

		newBankOperation.setLinkedAccount(itemToSave);
		newBankOperation.setTransactionDate(new Date(System.currentTimeMillis()));
		Double oldAmout = currentUser.getAccountFunds();
		Double newAmount = 0.0;

		if (depositTrueWithdrawFalse) {

			newAmount = oldAmout + withdrawMoney.getAmount();

		} else {

			newAmount = oldAmout - withdrawMoney.getAmount();
		}

		BigDecimal bd = new BigDecimal(newAmount).setScale(2, RoundingMode.HALF_UP);
		double finalAmountSaved = bd.doubleValue();

		currentUser.setAccountFunds(finalAmountSaved);
		userRepository.save(currentUser);
		
		BankOperation newItemBankOp = bankAccountRepository.save(newBankOperation);
		
		List<BankOperation> listing = itemToSave.getListOfOperationDoneOnThisAccount();
		
		listing.add(newItemBankOp);
			
		itemToSave.setListOfOperationDoneOnThisAccount(listing);
		externalBankRepository.save(itemToSave);
		
		

		List<BankOperation> theListInSession = (List<BankOperation>) session.getAttribute("listOfAllOperations");

		theListInSession.add(newItemBankOp);
		session.setAttribute("listOfAllOperations", theListInSession);

	}

	public boolean checkIfCurrentUserHasNecessaryFunds(Person currentUser, double d) {

		if (currentUser.getAccountFunds() - d >= 0) {

			return true;

		} else {

			return false;
		}

	}

	public boolean checkIfCurrentUserCanStillDepositToItsAccount(Person currentUser, double amount) {

		if (currentUser.getAccountFunds() + amount < 9999999) {

			return true;

		} else {

			return false;
		}

	}

	public boolean checkIfCurrentBankExistsInTheAttribute(Person currentUser, BankInformation bankInformation) {

		if (externalBankRepository.findByIbanAndBiccode(bankInformation.getIban(),
				bankInformation.getBicCode()) == null) {

			return false;

		} else {

			return true;
		}
	}

	@Transactional
	public void saveANewBankForCurrentUser(Person currentUser, BankInformation bankInformation, HttpSession session) {

		ExternalBankAccount newItem = new ExternalBankAccount();
		newItem.setBiccode(bankInformation.getBicCode());
		newItem.setIban(bankInformation.getIban());
		newItem.setLocation(bankInformation.getLocation());
		newItem.setAccountOwner(currentUser);
		newItem.setCurrency("EURO");
		newItem.setListOfOperationDoneOnThisAccount(new ArrayList<BankOperation>());

		logger.info("|||||A||||| TEST A in SAVE A NEW BANK BEFORE findbyId " + newItem);
		
		
		
		ExternalBankAccount testItem  = externalBankRepository.save(newItem);
		
		
		
		logger.info("|||||B||||| TEST A in SAVE A NEW BANK AFTER findbyId " + testItem);

		List<ExternalBankAccount> theListInSession = (List<ExternalBankAccount>) session.getAttribute("listOfAllBankAccountOwned");

		theListInSession.add(testItem);
		session.setAttribute("listOfAllBankAccountOwned", theListInSession);

	}
	
	public Map<Integer, String> createTheListForUIOperation(List<ExternalBankAccount> listOfAllBankAccountOwned) {
	
		
		Map<Integer, String> result = new HashMap<>();
		
		for(ExternalBankAccount eba : listOfAllBankAccountOwned) {
			
			
			result.put(eba.getId(), "BANKLocation : " + eba.getLocation() + ", IBAN : " + eba.getIban());
			
		}
		
		
		return result;
	}

	
	

	// ====== Setters of repository solely needed for testing purposes.
	// ====== Once the app is validated, and for security reasons, these setters
	// ====== can be deleted

	public void setBankAccountRepo(BankOperationRepository bankAccountRepo) {
		this.bankAccountRepository = bankAccountRepo;
	}

	public void setUserRepo(PersonRepository userRepo) {
		this.userRepository = userRepo;
	}

	public void setExternalBankRepository(ExternalBankRepository externalBankRepository) {
		this.externalBankRepository = externalBankRepository;
	}

	

}

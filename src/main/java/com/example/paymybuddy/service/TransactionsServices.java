package com.example.paymybuddy.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.paymybuddy.DTO.BuddiesInConnexion;
import com.example.paymybuddy.DTO.PaymentData;
import com.example.paymybuddy.constant.Fees;
import com.example.paymybuddy.model.ConnexionBetweenBuddies;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.model.Transaction;
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
	ConnexionBetweenBuddiesRepository connexionRepo;

	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TransactionsServices.class);

	public TransactionsServices() {
	}

	public boolean checkIfCurrentUserHasSufficientAmounts(Person currentUser, double d) {

		if (currentUser.getAmount() - d >= 0) {

			return true;

		} else {

			return false;
		}

	}

	public boolean checkIfGoingToBePayedBuddyDoesNotHaveTooMuchMoney(String id, double d) {

		Person buddy = personRepo.findById(Integer.parseInt(id)).get();

		if (buddy.getAmount() + d < 9999999) {

			return true;

		} else {

			return false;
		}

	}

	public Person findByEmailFromRepo(String email) {

		return personRepo.findByEmail(email);
	}

	public void adjustAccount(PaymentData Data, Person currentUser, HttpSession session) {

		Person theBud = personRepo.findById(Integer.parseInt(Data.getPersonToPay())).get();

		Double currentUserNewAmount = currentUser.getAmount() - (Data.getAmount() * (1 + Fees.CLASSIC_FEE_APP));
		Double buddyPaidNewAmount = theBud.getAmount() + Data.getAmount();

		BigDecimal currentUserAmountRounded = new BigDecimal(currentUserNewAmount).setScale(2, RoundingMode.HALF_UP);
		double currentUserAmountToSave = currentUserAmountRounded.doubleValue();

		BigDecimal buddyPayedRoundedAmount = new BigDecimal(buddyPaidNewAmount).setScale(2, RoundingMode.HALF_UP);
		double buddyPayedAmountToSave = buddyPayedRoundedAmount.doubleValue();

		currentUser.setAmount(currentUserAmountToSave);
		theBud.setAmount(buddyPayedAmountToSave);
		logger.info("IN ADJUSTACCOUNT, class BankAccountService, newUserAmount : " + currentUserNewAmount);
		logger.info("IN ADJUSTACCOUNT, class BankAccountService, newgonnaBePaidAccount : " + buddyPaidNewAmount);

		saveANewTransaction(currentUser, Data, theBud, session);
		personRepo.save(currentUser);
		personRepo.save(theBud);

	}

	public void saveANewTransaction(Person currentUser, PaymentData pay, Person it, HttpSession session) {

		Transaction newItem = new Transaction();
		newItem.setAmount(pay.getAmount());
		newItem.setCommentaire(pay.getDescription());
		newItem.setPayee(it);
		newItem.setPayeur(currentUser);
		transacRepo.save(newItem);

		List<Transaction> temporaryListOfTransaction = (List<Transaction>) session
				.getAttribute("listOfAllTransactions");
		temporaryListOfTransaction.add(newItem);
		session.setAttribute("listOfAllTransactions", temporaryListOfTransaction);
	}

	public Boolean addingABuddyToTheCurrentUser(BuddiesInConnexion bud, Person currentUser, HttpSession session) {

		Person buddyInConnection = personRepo.findByEmail(bud.getEmail());
		logger.info("===============" + buddyInConnection.getId());

		if (connexionRepo.findByIdOfCenterAndBuddyOfACenter(currentUser.getId(), buddyInConnection.getId()) == null
				&& buddyInConnection.getId() != currentUser.getId()) {

			ConnexionBetweenBuddies newConnexion = new ConnexionBetweenBuddies();
			newConnexion.setIdOfCenter(currentUser.getId());
			newConnexion.setBuddyOfACenter(buddyInConnection.getId());
			connexionRepo.save(newConnexion);
			List<ConnexionBetweenBuddies> theResult = (List<ConnexionBetweenBuddies>) session
					.getAttribute("listOfAllConnexionOfBuddies");
			theResult.add(newConnexion);
			session.setAttribute("listOfAllConnexionOfBuddies", theResult);
			return true;

		}

		return false;

	}

	public boolean checkIfThisPersonExistsInTheDB(BuddiesInConnexion bud) {

		if (personRepo.findByEmail(bud.getEmail()) != null) {

			return true;

		} else {

			return false;

		}

	}

	public Map<Person, String> createTheListOfBuddyForTransaction(
			List<ConnexionBetweenBuddies> listOfConnexionOfBuddies) {

		Map<Person, String> resultSet = new HashMap<>();

		for (ConnexionBetweenBuddies CB : listOfConnexionOfBuddies) {

			Person tempBuddy = personRepo.findById(CB.getBuddyOfACenter()).get();
			resultSet.put(tempBuddy, tempBuddy.getName() + ", " + tempBuddy.getLastName());

		}

		return resultSet;
	}

}

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

import com.example.paymybuddy.constant.Fees;
import com.example.paymybuddy.dto.BuddiesInConnexion;
import com.example.paymybuddy.dto.PaymentData;
import com.example.paymybuddy.model.ConnexionBetweenBuddies;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.model.Transaction;
import com.example.paymybuddy.repository.ConnexionBetweenBuddiesRepository;
import com.example.paymybuddy.repository.PersonRepository;
import com.example.paymybuddy.repository.TransactionRepository;

@Service
public class TransactionsServices {

	@Autowired
	TransactionRepository transacRepository;

	@Autowired
	PersonRepository personRepository;

	@Autowired
	ConnexionBetweenBuddiesRepository connexionRepository;

	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TransactionsServices.class);

	public TransactionsServices() {
	}

	public boolean checkIfCurrentUserHasSufficientAmounts(Person currentUser, double d) {

		if (currentUser.getAccountFunds() - d >= 0) {

			return true;

		} else {

			return false;
		}

	}

	public boolean checkIfGoingToBePayedBuddyDoesNotHaveTooMuchMoney(String id, double d) {

		Person buddy = personRepository.findById(Integer.parseInt(id)).get();

		if (buddy.getAccountFunds() + d < 9999999) {

			return true;

		} else {

			return false;
		}

	}

	public Person findByEmailFromRepo(String email) {

		return personRepository.findByEmail(email);
	}

	public void adjustTheAccountsBetweenBuddies(PaymentData Data, Person currentUser, HttpSession session) {

		Person theBud = personRepository.findById(Integer.parseInt(Data.getPersonToPay())).get();

		Double feePayedForThisTransaction = (Data.getAmount() * (Fees.CLASSIC_FEE_APP));

		Double currentUserNewAmount = currentUser.getAccountFunds() - (Data.getAmount() * (1 + Fees.CLASSIC_FEE_APP));
		Double buddyPaidNewAmount = theBud.getAccountFunds() + Data.getAmount();

		BigDecimal currentUserAmountRounded = new BigDecimal(currentUserNewAmount).setScale(2, RoundingMode.HALF_UP);
		double currentUserAmountToSave = currentUserAmountRounded.doubleValue();

		BigDecimal buddyPayedRoundedAmount = new BigDecimal(buddyPaidNewAmount).setScale(2, RoundingMode.HALF_UP);
		double buddyPayedAmountToSave = buddyPayedRoundedAmount.doubleValue();

		BigDecimal feePayedAmountRounded = new BigDecimal(feePayedForThisTransaction).setScale(2, RoundingMode.HALF_UP);
		double roundedFeePayedForThisTransaction = feePayedAmountRounded.doubleValue();

		currentUser.setAccountFunds(currentUserAmountToSave);

		double pastFeePayed = currentUser.getTotalamountpayedfee();
		currentUser.setTotalamountpayedfee(pastFeePayed + roundedFeePayedForThisTransaction);

		theBud.setAccountFunds(buddyPayedAmountToSave);

		saveANewTransaction(currentUser, Data, theBud, session, roundedFeePayedForThisTransaction);
		personRepository.save(currentUser);
		personRepository.save(theBud);

	}

	public void saveANewTransaction(Person currentUser, PaymentData pay, Person it, HttpSession session,
			double roundedFeePayedForThisTransaction) {

		Transaction newItem = new Transaction();
		newItem.setAmount(pay.getAmount());
		newItem.setCommentaire(pay.getDescription());
		newItem.setPayee(it);
		newItem.setPayer(currentUser);
		newItem.setFeeOnTransaction(roundedFeePayedForThisTransaction);
		transacRepository.save(newItem);

		List<Transaction> temporaryListOfTransaction = (List<Transaction>) session
				.getAttribute("listOfAllTransactions");
		temporaryListOfTransaction.add(newItem);
		session.setAttribute("listOfAllTransactions", temporaryListOfTransaction);
	}

	public Boolean addingABuddyToTheCurrentUser(BuddiesInConnexion bud, Person currentUser, HttpSession session) {

		Person buddyInConnection = personRepository.findByEmail(bud.getEmail());

		if (buddyInConnection != null) {

			if (connexionRepository.findByIdOfCenterAndBuddyOfACenter(currentUser.getId(), buddyInConnection.getId()) == null
					&& buddyInConnection.getId() != currentUser.getId()) {

				ConnexionBetweenBuddies newConnexion = new ConnexionBetweenBuddies();
				newConnexion.setIdOfCenter(currentUser.getId());
				newConnexion.setBuddyOfACenter(buddyInConnection.getId());
				connexionRepository.save(newConnexion);
				List<ConnexionBetweenBuddies> theResult = (List<ConnexionBetweenBuddies>) session
						.getAttribute("listOfAllConnexionOfBuddies");
				theResult.add(newConnexion);
				session.setAttribute("listOfAllConnexionOfBuddies", theResult);
				return true;

			}

		}

		return false;

	}

	public boolean checkIfThisPersonExistsInTheDB(BuddiesInConnexion bud) {

		if (personRepository.findByEmail(bud.getEmail()) != null) {

			return true;

		} else {

			return false;

		}

	}

	public Map<Person, String> createTheListOfBuddyForTransaction(
			List<ConnexionBetweenBuddies> listOfConnexionOfBuddies) {

		Map<Person, String> resultSet = new HashMap<>();

		for (ConnexionBetweenBuddies CB : listOfConnexionOfBuddies) {

			Person tempBuddy = personRepository.findById(CB.getBuddyOfACenter()).get();
			resultSet.put(tempBuddy, tempBuddy.getName() + ", " + tempBuddy.getLastName());

		}

		return resultSet;
	}

	// ====== Setters of repository solely needed for testing purposes.
	// ====== Once the app is validated, and for security reasons, these setters
	// ====== can be deleted

	public void setPersonRepo(PersonRepository personRepo) {
		this.personRepository = personRepo;
	}


	public void setTransacRepo(TransactionRepository transacRepo) {
		this.transacRepository = transacRepo;
	}

	public void setConnexionRepo(ConnexionBetweenBuddiesRepository connexionRepo) {
		this.connexionRepository = connexionRepo;
	}

}

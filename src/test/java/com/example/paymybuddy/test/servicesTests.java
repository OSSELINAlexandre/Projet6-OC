package com.example.paymybuddy.test;



import static org.hamcrest.CoreMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;

import com.example.paymybuddy.DTO.BuddiesInConnexion;
import com.example.paymybuddy.DTO.PaymentData;
import com.example.paymybuddy.model.ConnexionBetweenBuddies;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.model.Transaction;
import com.example.paymybuddy.repository.ConnexionBetweenBuddiesRepository;
import com.example.paymybuddy.repository.PersonRepository;
import com.example.paymybuddy.repository.TransactionRepository;
import com.example.paymybuddy.service.OperationOnAccountServices;
import com.example.paymybuddy.service.TransactionsServices;
import com.example.paymybuddy.service.UserServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class servicesTests {

	@Autowired
	OperationOnAccountServices operationServices;

	@Autowired
	TransactionsServices transacServices;

	@Autowired
	UserServices userServices;

	public Person currentUser;
	public Person buddyUser;
	
	@Mock
	PersonRepository personRepo;
	
	@Mock
	TransactionRepository transacRepo;
	
	@Mock
	ConnexionBetweenBuddiesRepository connexionRepo;
	
	
	@Mock
	HttpSession session;
	
	final ArgumentCaptor<Transaction> transactionCaptor =
		    ArgumentCaptor.forClass(Transaction.class);
	
	final ArgumentCaptor<ConnexionBetweenBuddies> connexionCaptor =
		    ArgumentCaptor.forClass(ConnexionBetweenBuddies.class);



// =========================================================================================
// ================	            TESTS FOR OperationOnAccountServices        ================	
// =========================================================================================

	@BeforeEach
	public void init() {


		currentUser = new Person();
		currentUser.setId(1);
		currentUser.setName("Brad");
		currentUser.setLastName("Pitt");
		currentUser.setAccountfunds(150.00);
		currentUser.setEmail("brad.pitt@gmail.com");
		currentUser.setPassword("imbradpitt");
		
		
		buddyUser = new Person();
		buddyUser.setId(2);
		buddyUser.setName("Jean");
		buddyUser.setLastName("Valjean");
		buddyUser.setAccountfunds(350.00);
		buddyUser.setEmail("galere.lumiere@gmail.com");
		buddyUser.setPassword("imvaljean");
		
		
	}

	@Test
	public void checkIfCurrentUserHasNecessaryFunds_ShouldReturnTrue_IfNecessaryFunds() {

		Boolean actual = operationServices.checkIfCurrentUserHasNecessaryFunds(currentUser, 120.00);
		assertEquals(actual, true);

	}

	@Test
	public void checkIfCurrentUserHasNecessaryFunds_ShouldReturnFalse_IfNotNecessaryFunds() {

		Boolean actual = operationServices.checkIfCurrentUserHasNecessaryFunds(currentUser, 999999999999.99);
		assertEquals(actual, false);

	}

	@Test
	public void checkIfCurrentUserCanStillDepositToItsAccount_ShouldReturnTrue_IfNecessaryFunds() {


		
		Boolean actual = operationServices.checkIfCurrentUserCanStillDepositToItsAccount(currentUser, 120.00);
		assertEquals(actual, true);

	}

	@Test
	public void checkIfCurrentUserCanStillDepositToItsAccount_ShouldReturnFalse_IfNotNecessaryFunds() {

		Boolean actual = operationServices.checkIfCurrentUserCanStillDepositToItsAccount(currentUser, 999999999999.99);
		assertEquals(actual, false);

	}

// =========================================================================================
// ================	                    TESTS FOR transacServices           ================	
// =========================================================================================

	@Test
	public void checkIfcheckIfCurrentUserHasSufficientAmounts_ShouldReturnTrue_IfNecessaryFunds() {

		Boolean actual = transacServices.checkIfCurrentUserHasSufficientAmounts(currentUser, 120.00);
		assertEquals(actual, true);

	}

	@Test
	public void checkIfcheckIfCurrentUserHasSufficientAmounts_ShouldReturnFalse_IfNotNecessaryFunds() {

		Boolean actual = transacServices.checkIfCurrentUserHasSufficientAmounts(currentUser, 999999999999.99);
		assertEquals(actual, false);

	}

	@Test
	public void checkIfGoingToBePayedBuddyDoesNotHaveTooMuchMoney_ShouldReturnTrue_IfNecessaryFunds() {

		Optional<Person> copyOfCurrentUser = Optional.of(currentUser);
		when(personRepo.findById(1)).thenReturn(copyOfCurrentUser);
		transacServices.setPersonRepo(personRepo);
		
		Boolean actual = transacServices.checkIfGoingToBePayedBuddyDoesNotHaveTooMuchMoney("1", 120.00);
		assertEquals(actual, true);

		
	}

	@Test
	public void checkIfGoingToBePayedBuddyDoesNotHaveTooMuchMoney_ShouldReturnFalse_IfNotNecessaryFunds() {
		
		Optional<Person> copyOfCurrentUser = Optional.of(currentUser);
		when(personRepo.findById(1)).thenReturn(copyOfCurrentUser);
		transacServices.setPersonRepo(personRepo);
		Boolean actual = transacServices.checkIfGoingToBePayedBuddyDoesNotHaveTooMuchMoney("1", 999999999999.99);
		assertEquals(actual, false);

		
	}
	
	
	
	@Test
	public void adjustAccount_ShouldCallTheRepositories() {
		PaymentData pay = new PaymentData();
		pay.setPersonToPay("2");
		pay.setAmount(10.00);
		pay.setDescription("Testing the service");
		
		Transaction testItem = new Transaction();
		testItem.setId(1); // Je suis pas sûr pour celle-ci. 
		testItem.setCommentaire("Testing the service");
		testItem.setAmount(10.00);
		testItem.setPayeur(currentUser);
		testItem.setPayee(buddyUser);
	
		when(session.getAttribute("listOfAllTransactions")).thenReturn(new ArrayList<Transaction>());
	
		Transaction newItem = new Transaction();
		newItem.setAmount(pay.getAmount());
		newItem.setCommentaire(pay.getDescription());
		newItem.setPayee(buddyUser);
		newItem.setPayeur(currentUser);	
		Optional<Person> buddyOfUser = Optional.of(buddyUser);
		when(personRepo.findById(2)).thenReturn(buddyOfUser);
		transacServices.setPersonRepo(personRepo);
		transacServices.setTransacRepo(transacRepo);
	
		transacServices.adjustAccount(pay, currentUser, session);
		
		verify(personRepo).save(buddyUser);
		verify(personRepo).save(currentUser);

		verify(transacRepo, times(1)).save(transactionCaptor.capture());
		Transaction actual = transactionCaptor.getValue();
		assertTrue(actual.getCommentaire().equals(newItem.getCommentaire()));
		
	}

	
	@Test
	public void addingABuddyToTheCurrentUsers_ShouldReturnFalse_WhenPersonExists() {

		transacServices.setPersonRepo(personRepo);
		
		BuddiesInConnexion testBuddy = new BuddiesInConnexion();
		testBuddy.setEmail("galere.lumiere@gmail.com");
	
		when(personRepo.findByEmail("galere.lumiere@gmail.com")).thenReturn(null);
		Boolean actualResult = transacServices.addingABuddyToTheCurrentUser(testBuddy, currentUser, session);
		assertTrue(!actualResult);

	}

	@Test
	public void  addingABuddyToTheCurrentUsers_ShouldReturnTrueAndCallMethods_WhenPersonExists() {
		
		transacServices.setPersonRepo(personRepo);
		transacServices.setConnexionRepo(connexionRepo);
		
		BuddiesInConnexion testBuddy = new BuddiesInConnexion();
		testBuddy.setEmail("galere.lumiere@gmail.com");
		
		when(personRepo.findByEmail("galere.lumiere@gmail.com")).thenReturn(buddyUser);
		when(connexionRepo.findByIdOfCenterAndBuddyOfACenter(1, 2)).thenReturn(null);

		
		when(session.getAttribute("listOfAllConnexionOfBuddies")).thenReturn(new ArrayList<ConnexionBetweenBuddies>());

		
		
		Boolean actualResult = transacServices.addingABuddyToTheCurrentUser(testBuddy, currentUser, session);
		

		verify(connexionRepo, times(1)).save(connexionCaptor.capture());
		ConnexionBetweenBuddies actual = connexionCaptor.getValue();
		
		
		assertTrue(actual.getIdOfCenter() == 1);
		assertTrue(actual.getBuddyOfACenter() == 2);
		assertTrue(actualResult);


	}
	
	@Test
	public void TestcreateTheListOfBuddyForTransaction_shouldReturnExpectedDesign() {
		
		transacServices.setPersonRepo(personRepo);

		
		List<ConnexionBetweenBuddies> testList = new ArrayList<ConnexionBetweenBuddies>();
		ConnexionBetweenBuddies connexTest = new ConnexionBetweenBuddies();
		connexTest.setBuddyOfACenter(2);
		connexTest.setId(1);
		connexTest.setIdOfCenter(1);
		
		testList.add(connexTest);

		Map<Person, String> resultTestItem = new HashMap<>();
		Optional<Person> copyOfBuddy = Optional.of(buddyUser);
		
		when(personRepo.findById(2)).thenReturn(copyOfBuddy);
		
		resultTestItem = transacServices.createTheListOfBuddyForTransaction(testList);
		assertTrue(resultTestItem.containsValue(buddyUser.getName() + ", " + buddyUser.getLastName()));

			
		
	}
	
	
// =========================================================================================
// ================	                    TESTS FOR UserServices              ================	
// =========================================================================================
	



	
	
	
}

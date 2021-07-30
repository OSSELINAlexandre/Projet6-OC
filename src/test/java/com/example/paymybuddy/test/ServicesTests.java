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

import com.example.paymybuddy.dto.AdminDataForDashboard;
import com.example.paymybuddy.dto.BankAccountWithdrawalDepositInformation;
import com.example.paymybuddy.dto.BankInformation;
import com.example.paymybuddy.dto.BuddiesInConnexion;
import com.example.paymybuddy.dto.LoginRegistration;
import com.example.paymybuddy.dto.PaymentData;
import com.example.paymybuddy.model.BankOperation;
import com.example.paymybuddy.model.ConnectionBetweenBuddies;
import com.example.paymybuddy.model.ExternalBankAccount;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.model.Transaction;
import com.example.paymybuddy.repository.BankOperationRepository;
import com.example.paymybuddy.repository.ConnexionBetweenBuddiesRepository;
import com.example.paymybuddy.repository.ExternalBankRepository;
import com.example.paymybuddy.repository.PersonRepository;
import com.example.paymybuddy.repository.TransactionRepository;
import com.example.paymybuddy.service.AdminServices;
import com.example.paymybuddy.service.OperationOnAccountServices;
import com.example.paymybuddy.service.TransactionsServices;
import com.example.paymybuddy.service.UserServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ServicesTests {

	@Autowired
	OperationOnAccountServices operationServices;

	@Autowired
	TransactionsServices transacServices;

	@Autowired
	UserServices userServices;
	
	@Autowired
	AdminServices adminServices;

	public Person currentUser;
	public Person buddyUser;
	public ExternalBankAccount externalAccount;

	@Mock
	PersonRepository personRepo;

	@Mock
	TransactionRepository transacRepo;

	@Mock
	ConnexionBetweenBuddiesRepository connexionRepo;

	@Mock
	BankOperationRepository bankAccountRepo;
	
	@Mock
	ExternalBankRepository externalAccountRepo;

	@Mock
	HttpSession session;

	final ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

	final ArgumentCaptor<ConnectionBetweenBuddies> connexionCaptor = ArgumentCaptor
			.forClass(ConnectionBetweenBuddies.class);

	final ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);

	final ArgumentCaptor<BankOperation> bankOperationCaptor = ArgumentCaptor.forClass(BankOperation.class);

	final ArgumentCaptor<Person> personCaptorTwo = ArgumentCaptor.forClass(Person.class);
	
	
	final ArgumentCaptor<ExternalBankAccount> externalAccountCaptor = ArgumentCaptor.forClass(ExternalBankAccount.class);
	final ArgumentCaptor<String> externalAccountStringCaptor = ArgumentCaptor.forClass(String.class);

	@BeforeEach
	public void init() {

		currentUser = new Person();
		currentUser.setId(1);
		currentUser.setName("Brad");
		currentUser.setLastName("Pitt");
		currentUser.setAccountFunds(150.00);
		currentUser.setEmail("brad.pitt@gmail.com");
		currentUser.setPassword("imbradpitt");
		currentUser.setAuthority("USER");
		currentUser.setTotalamountpayedfee(150.00);

		
		buddyUser = new Person();
		buddyUser.setId(2);
		buddyUser.setName("Jean");
		buddyUser.setLastName("Valjean");
		buddyUser.setAccountFunds(350.00);
		buddyUser.setEmail("galere.lumiere@gmail.com");
		buddyUser.setPassword("imvaljean");
		buddyUser.setAuthority("USER");
		buddyUser.setTotalamountpayedfee(750.00);
		
		
		externalAccount = new ExternalBankAccount();
		externalAccount.setAccountOwner(currentUser);
		externalAccount.setBiccode("ABC");
		externalAccount.setCurrency("EURO");
		externalAccount.setIban("FRG");
		externalAccount.setId(1);
		externalAccount.setListOfOperationDoneOnThisAccount(new ArrayList<BankOperation>());
		externalAccount.setLocation("Avignon");


	}

// =========================================================================================
// ================	            TESTS FOR OperationOnAccountServices        ================	
// =========================================================================================

	@Test
	public void TestsaveForDeposit_ShouldCallAllRepository() {

		operationServices.setUserRepo(personRepo);
		operationServices.setBankAccountRepo(bankAccountRepo);
		operationServices.setExternalBankRepository(externalAccountRepo);
		
		BankAccountWithdrawalDepositInformation testItem = new BankAccountWithdrawalDepositInformation();
		testItem.setAmount(150.00);
		testItem.setBankAccountToDoOperationID("1");
		
		Optional<ExternalBankAccount> testingItem = Optional.of(externalAccount);
		when(session.getAttribute("listOfAllOperations")).thenReturn(new ArrayList<BankOperation>());
		when(externalAccountRepo.findById(1)).thenReturn(testingItem);


		operationServices.saveForDepositorWithdrawal(currentUser, testItem, true, session);

		verify(personRepo, times(1)).save(personCaptorTwo.capture());
		verify(bankAccountRepo, times(1)).save(bankOperationCaptor.capture());
		BankOperation actualOperation = bankOperationCaptor.getValue();
		Person actualPerson = personCaptorTwo.getValue();

		assertTrue(currentUser.getAccountFunds() == actualPerson.getAccountFunds());
		assertTrue(actualOperation.getHolder().getId() == currentUser.getId() && actualOperation.getAmount() == 150.00);

	}

	@Test
	public void TestsaveForWithdrawal_ShouldCallAllRepository() {

		operationServices.setUserRepo(personRepo);
		operationServices.setBankAccountRepo(bankAccountRepo);
		when(session.getAttribute("listOfAllOperations")).thenReturn(new ArrayList<BankOperation>());
		BankAccountWithdrawalDepositInformation testItem = new BankAccountWithdrawalDepositInformation();
		testItem.setAmount(150.00);
		testItem.setBankAccountToDoOperationID("1");

		
		
		operationServices.saveForDepositorWithdrawal(currentUser, testItem, false, session);

		verify(personRepo, times(1)).save(personCaptorTwo.capture());
		verify(bankAccountRepo, times(1)).save(bankOperationCaptor.capture());
		BankOperation actualOperation = bankOperationCaptor.getValue();
		Person actualPerson = personCaptorTwo.getValue();

		assertTrue(currentUser.getAccountFunds() == actualPerson.getAccountFunds());
		assertTrue(actualOperation.getHolder().getId() == currentUser.getId() && actualOperation.getAmount() == 150.00);

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
	
	@Test
	public void test_saveANewBankForCurrentUser(){
		
		operationServices.setExternalBankRepository(externalAccountRepo);
		BankInformation bankInfo = new BankInformation();
		bankInfo.setBankName("Agricole Credit");
		bankInfo.setBicCode("DEF");
		bankInfo.setIban("ABCDEF");
		bankInfo.setLocation("Paris");
		
		ExternalBankAccount testItem = new ExternalBankAccount();
		testItem.setAccountOwner(buddyUser);
		testItem.setBankname("Agricole Credit");
		testItem.setBiccode("DEF");
		testItem.setCurrency("EURO");
		testItem.setIban("ABCDEF");
		testItem.setId(1);
		testItem.setListOfOperationDoneOnThisAccount(new ArrayList<BankOperation>());
		testItem.setLocation("Paris");
		
		ExternalBankAccount testItemII = new ExternalBankAccount();
		testItemII.setAccountOwner(buddyUser);
		testItemII.setBankname("Agricole Credit");
		testItemII.setBiccode("DEF");
		testItemII.setCurrency("EURO");
		testItemII.setIban("ABCDEF");
		testItemII.setListOfOperationDoneOnThisAccount(new ArrayList<BankOperation>());
		testItemII.setLocation("Paris");
		
		
		List<ExternalBankAccount> testIngList = new ArrayList<ExternalBankAccount>();
		when(session.getAttribute("listOfAllBankAccountOwned")).thenReturn(testIngList);
		
		operationServices.saveANewBankForCurrentUser(currentUser, bankInfo, session);
		
		verify(session, times(1)).setAttribute(externalAccountStringCaptor.capture(), externalAccountCaptor.capture());
		
		List<ExternalBankAccount> actual = externalAccountCaptor.getAllValues();
		assertTrue(actual.size() == 1);
		
		
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
		testItem.setPayer(currentUser);
		testItem.setPayee(buddyUser);

		when(session.getAttribute("listOfAllTransactions")).thenReturn(new ArrayList<Transaction>());

		Transaction newItem = new Transaction();
		newItem.setAmount(pay.getAmount());
		newItem.setCommentaire(pay.getDescription());
		newItem.setPayee(buddyUser);
		newItem.setPayer(currentUser);
		Optional<Person> buddyOfUser = Optional.of(buddyUser);
		when(personRepo.findById(2)).thenReturn(buddyOfUser);
		transacServices.setPersonRepo(personRepo);
		transacServices.setTransacRepo(transacRepo);

		transacServices.adjustTheAccountsBetweenBuddies(pay, currentUser, session);

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
	public void addingABuddyToTheCurrentUsers_ShouldReturnTrueAndCallMethods_WhenPersonExists() {

		transacServices.setPersonRepo(personRepo);
		transacServices.setConnexionRepo(connexionRepo);

		BuddiesInConnexion testBuddy = new BuddiesInConnexion();
		testBuddy.setEmail("galere.lumiere@gmail.com");

		when(personRepo.findByEmail("galere.lumiere@gmail.com")).thenReturn(buddyUser);
		when(connexionRepo.findByIdOfCenterAndBuddyOfACenter(1, 2)).thenReturn(null);

		when(session.getAttribute("listOfAllConnexionOfBuddies")).thenReturn(new ArrayList<ConnectionBetweenBuddies>());

		Boolean actualResult = transacServices.addingABuddyToTheCurrentUser(testBuddy, currentUser, session);

		verify(connexionRepo, times(1)).save(connexionCaptor.capture());
		ConnectionBetweenBuddies actual = connexionCaptor.getValue();

		assertTrue(actual.getIdOfCenter() == 1);
		assertTrue(actual.getBuddyOfACenter() == 2);
		assertTrue(actualResult);

	}

	@Test
	public void TestcreateTheListOfBuddyForTransaction_shouldReturnExpectedDesign() {

		transacServices.setPersonRepo(personRepo);

		List<ConnectionBetweenBuddies> testList = new ArrayList<ConnectionBetweenBuddies>();
		ConnectionBetweenBuddies connexTest = new ConnectionBetweenBuddies();
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

	@Test
	public void TestsaveANewPersonInTheDB_shouldCallSaveMethod_AndEncryptPassword() {

		userServices.setPersonRepo(personRepo);

		LoginRegistration testItem = new LoginRegistration();
		testItem.seteMail("alexandre.osselin@gmail.com");
		testItem.setLastName("Osselin");
		testItem.setName("Alexandre");
		testItem.setPassword("YHW");
		testItem.setSecondTestPassword("YHW");

		userServices.saveANewPersonInTheDB(testItem);

		verify(personRepo, times(1)).save(personCaptor.capture());
		Person actual = personCaptor.getValue();

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		assertTrue(!actual.getPassword().equals("YHW"));
		assertTrue(encoder.matches("YHW", actual.getPassword()));

	}
	
	
// =========================================================================================
// ====================	             TESTS FOR AdminServices          ======================	
// =========================================================================================
	
	
	@Test 
	public void testinggenerateDashBoard_ShouldReturnAdequateReusult() {
		
		
		adminServices.setPersonRepo(personRepo);
		adminServices.setTransactionRepo(transacRepo);
		
		
		Transaction transac1 = new Transaction();
		Transaction transac2 = new Transaction();
		Transaction transac3 = new Transaction();
		Transaction transac4 = new Transaction();
		
		List<Transaction> testItem = new ArrayList<>();
		testItem.add(transac4);
		testItem.add(transac3);
		testItem.add(transac2);
		testItem.add(transac1);

		ArrayList<Person> listingIt = new ArrayList<Person>();
		listingIt.add(currentUser);
		Iterable<Person> testingItem = listingIt;
				
		when(personRepo.findAll()).thenReturn(testingItem);
		when(transacRepo.findByPayer(currentUser)).thenReturn(testItem);
		

		List<AdminDataForDashboard> actual = adminServices.generateDashBoard();

		assertTrue(actual.get(0).getFeePayed().equals(150.00) && actual.get(0).getNumberOfTransac() == 4);
		
	}


}

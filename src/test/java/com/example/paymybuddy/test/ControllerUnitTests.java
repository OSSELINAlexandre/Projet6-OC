package com.example.paymybuddy.test;

import javax.servlet.http.HttpSession;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;
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

import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.example.paymybuddy.constant.Fees;
import com.example.paymybuddy.controller.AdminDashBoardController;
import com.example.paymybuddy.controller.OperationController;
import com.example.paymybuddy.controller.TransferController;
import com.example.paymybuddy.controller.UserController;
import com.example.paymybuddy.dto.BankAccountWithdrawalDepositInformation;
import com.example.paymybuddy.dto.BuddiesInConnexion;
import com.example.paymybuddy.dto.LoginRegistration;
import com.example.paymybuddy.dto.PaymentData;
import com.example.paymybuddy.model.BankOperation;
import com.example.paymybuddy.model.ConnexionBetweenBuddies;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.model.Transaction;
import com.example.paymybuddy.repository.ConnexionBetweenBuddiesRepository;
import com.example.paymybuddy.repository.PersonRepository;
import com.example.paymybuddy.repository.TransactionRepository;
import com.example.paymybuddy.service.AdminServices;
import com.example.paymybuddy.service.OperationOnAccountServices;
import com.example.paymybuddy.service.TransactionsServices;
import com.example.paymybuddy.service.UserServices;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ControllerUnitTests {

	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(ControllerUnitTests.class);

	@Autowired
	UserController userController;

	@Autowired
	OperationController operationController;

	@Autowired
	TransferController transferController;

	@Autowired
	HttpSession session;

	@Mock
	HttpSession sessionMock;

	@Mock
	Model model;

	@Mock
	AdminServices adminServices;

	@Mock
	OperationOnAccountServices operationOnAccountServices;

	@Mock
	TransactionsServices transacServices;

	@Mock
	UserServices userServices;

	private Person currentUser;
	private List<Transaction> listOfAllTransactions;
	private List<BankOperation> listOfAllOperations;
	private List<ConnexionBetweenBuddies> listOfAllConnexionOfBuddies;

	@BeforeEach
	public void init() {

		currentUser = new Person();
		currentUser.setId(1);
		currentUser.setName("Brad");
		currentUser.setLastName("Pitt");
		currentUser.setAccountFunds(150.00);
		currentUser.setEmail("brad.pitt@gmail.com");
		currentUser.setPassword("imbradpitt");
		currentUser.setAuthority("ADMIN");
		currentUser.setTotalamountpayedfee(150.00);

		listOfAllTransactions = new ArrayList<Transaction>();
		listOfAllOperations = new ArrayList<BankOperation>();
		listOfAllConnexionOfBuddies = new ArrayList<ConnexionBetweenBuddies>();

		currentUser.setListOfALLOperations(listOfAllOperations);
		currentUser.setListOfBuddies(listOfAllConnexionOfBuddies);
		currentUser.setTransactionsThatWerePayed(listOfAllTransactions);
		currentUser.setTransactionsPayed(listOfAllTransactions);

		userController.setCurrentUser(currentUser);
		userController.setListOfAllConnexionOfBuddies(listOfAllConnexionOfBuddies);
		userController.setListOfAllOperations(listOfAllOperations);
		userController.setListOfAllTransactions(listOfAllTransactions);
		userController.setUserServices(userServices);

		transferController.setCurrentUser(currentUser);
		transferController.setListOfAllConnexionOfBuddies(listOfAllConnexionOfBuddies);
		transferController.setListOfAllTransactions(listOfAllTransactions);
		transferController.setTransactionServices(transacServices);
		
		
		operationController.setBankAccountServices(operationOnAccountServices);
		operationController.setCurrentUser(currentUser);
		operationController.setListOfAllOperations(listOfAllOperations);

	}

	// =======================================================================================================
	// ================================== TEST FOR UserController
	// ============================================
	// =======================================================================================================

	@Test
	@WithMockUser(username = "barack.obama@gmail.com", password = "test", authorities = "ADMIN")
	public void test_setAllTheDataFromAuthenticatedUser() {

		when(userServices.getThePersonAfterAuthentication("barack.obama@gmail.com")).thenReturn(currentUser);

		ModelAndView theView = userController.setAllTheDataFromAuthenticatedUser(session);

		assertTrue(theView.getViewName().toString().equals("redirect:/userHome"));

	}

	@Test
	public void Test_registeringNewPerson() {

		Optional<Boolean> testItem = Optional.of(true);
		String result = userController.registeringNewPerson(testItem, testItem, model, session);

		assertTrue(result.equals("register"));

	}

	@Test
	public void Test_processRegistration_WheneMailExists() {

		LoginRegistration loginReg = new LoginRegistration();
		when(userServices.checkIfTheEmailExistsInTheDB(loginReg)).thenReturn(true);

		ModelAndView theView = userController.processRegistration(loginReg, session, model);
		assertTrue(theView.getViewName().toString().equals("redirect:/register")
				&& theView.getModel().containsKey("existingmail"));

	}

	@Test
	public void Test_processRegistration_WhenTwoSamePassword() {

		LoginRegistration loginReg = new LoginRegistration();
		loginReg.setPassword("WRONG");
		loginReg.setSecondTestPassword("FALSE");
		when(userServices.checkIfTheEmailExistsInTheDB(loginReg)).thenReturn(false);

		ModelAndView theView = userController.processRegistration(loginReg, session, model);
		assertTrue(theView.getViewName().toString().equals("redirect:/register")
				&& theView.getModel().containsKey("passwordmatch"));

	}

	@Test
	public void Test_processRegistration_WhenEverythingIsAllright() {

		LoginRegistration loginReg = new LoginRegistration();
		loginReg.setPassword("TRUE");
		loginReg.setSecondTestPassword("TRUE");
		when(userServices.checkIfTheEmailExistsInTheDB(loginReg)).thenReturn(false);

		ModelAndView theView = userController.processRegistration(loginReg, session, model);
		assertTrue(theView.getViewName().toString().equals("redirect:/registersucessfully"));

	}

	// =======================================================================================================
	// ================================== TEST FOR TransferController
	// ========================================
	// =======================================================================================================

	@Test
	@WithMockUser(username = "barack.obama@gmail.com", password = "test", authorities = "ADMIN")
	public void test_returnTransferPage() {

		when(sessionMock.getAttribute("listOfAllTransactions")).thenReturn(listOfAllTransactions);
		when(sessionMock.getAttribute("listOfAllConnexionOfBuddies")).thenReturn(listOfAllConnexionOfBuddies);
		when(sessionMock.getAttribute("currentUser")).thenReturn(currentUser);

		when(transacServices.createTheListOfBuddyForTransaction(listOfAllConnexionOfBuddies))
				.thenReturn(new HashMap<Person, String>());

		Optional<Boolean> testItem = Optional.of(true);
		String theView = transferController.returnTransferPage(testItem, testItem, testItem, testItem, testItem, model,
				sessionMock);

		assertTrue(theView.equals("transfer_page"));

	}

	@Test
	public void Test_processThePaymentBetweenBuddies_ShouldProcessErrorIfNoSufficientAmount() {

		PaymentData payItem = new PaymentData();
		payItem.setAmount(10000.00);
		payItem.setDescription("Testing Purpose");
		payItem.setPersonToPay("1");

		when(transacServices.checkIfCurrentUserHasSufficientAmounts(currentUser,
				payItem.getAmount() * (1 + Fees.CLASSIC_FEE_APP))).thenReturn(false);

		ModelAndView theView = transferController.processThePaymentBetweenBuddies(payItem, model, session);

		assertTrue(theView.getViewName().toString().equals("redirect:/transfer")
				&& theView.getModel().containsKey("errorPayingBuddy"));

	}

	@Test
	public void Test_processThePaymentBetweenBuddies_ShouldProcessErrorIfNoTooMuchMoney() {

		PaymentData payItem = new PaymentData();
		payItem.setAmount(10000.00);
		payItem.setDescription("Testing Purpose");
		payItem.setPersonToPay("1");

		when(transacServices.checkIfCurrentUserHasSufficientAmounts(currentUser,
				payItem.getAmount() * (1 + Fees.CLASSIC_FEE_APP))).thenReturn(true);
		when(transacServices.checkIfGoingToBePayedBuddyDoesNotHaveTooMuchMoney(payItem.getPersonToPay(),
				payItem.getAmount())).thenReturn(false);

		ModelAndView theView = transferController.processThePaymentBetweenBuddies(payItem, model, session);

		assertTrue(theView.getViewName().toString().equals("redirect:/transfer")
				&& theView.getModel().containsKey("yourBuddyHasTooMuchMoney"));

	}

	@Test
	public void Test_processThePaymentBetweenBuddies_ShouldCallAdjustTheAccount() {

		PaymentData payItem = new PaymentData();
		payItem.setAmount(10000.00);
		payItem.setDescription("Testing Purpose");
		payItem.setPersonToPay("1");

		when(transacServices.checkIfCurrentUserHasSufficientAmounts(currentUser,
				payItem.getAmount() * (1 + Fees.CLASSIC_FEE_APP))).thenReturn(true);
		when(transacServices.checkIfGoingToBePayedBuddyDoesNotHaveTooMuchMoney(payItem.getPersonToPay(),
				payItem.getAmount())).thenReturn(true);

		ModelAndView theView = transferController.processThePaymentBetweenBuddies(payItem, model, session);

		verify(transacServices, times(1)).adjustTheAccountsBetweenBuddies(payItem, currentUser, session);

		assertTrue(theView.getViewName().toString().equals("redirect:/transfer"));

	}

	@Test
	public void Test_verificationOfExistenceOfBuddyForTransfer_ShouldSendErrorCannotAddYourself() {

		BuddiesInConnexion testItem = new BuddiesInConnexion();
		testItem.setEmail("barack.obama@gmail.com");

		when(transacServices.addingABuddyToTheCurrentUser(testItem, currentUser, session)).thenReturn(false);
		when(transacServices.checkIfThisPersonExistsInTheDB(testItem)).thenReturn(true);
		when(transacServices.findByEmailFromRepo(testItem.getEmail())).thenReturn(currentUser);

		ModelAndView theView = transferController.verificationOfExistenceOfBuddyForTransfer(testItem, session, model);

		assertTrue(theView.getViewName().toString().equals("redirect:/transfer")
				&& theView.getModel().containsKey("cannotAddYourself"));

	}

	@Test
	public void Test_verificationOfExistenceOfBuddyForTransfer_ShouldSendErroralreadyHavingBuddy() {

		BuddiesInConnexion testItem = new BuddiesInConnexion();
		testItem.setEmail("barack.obama@gmail.com");
		Person newPers = new Person();
		newPers.setId(5);
		when(transacServices.addingABuddyToTheCurrentUser(testItem, currentUser, session)).thenReturn(false);
		when(transacServices.checkIfThisPersonExistsInTheDB(testItem)).thenReturn(true);
		when(transacServices.findByEmailFromRepo(testItem.getEmail())).thenReturn(newPers);

		ModelAndView theView = transferController.verificationOfExistenceOfBuddyForTransfer(testItem, session, model);

		assertTrue(theView.getViewName().toString().equals("redirect:/transfer") && theView.getModel().containsKey("alreadyHavingBuddy"));

	}

	@Test
	public void Test_verificationOfExistenceOfBuddyForTransfer_ShouldSendErrorFindingBuddy() {

		BuddiesInConnexion testItem = new BuddiesInConnexion();
		testItem.setEmail("barack.obama@gmail.com");
		Person newPers = new Person();
		newPers.setId(5);
		when(transacServices.addingABuddyToTheCurrentUser(testItem, currentUser, session)).thenReturn(false);
		when(transacServices.checkIfThisPersonExistsInTheDB(testItem)).thenReturn(false);

		ModelAndView theView = transferController.verificationOfExistenceOfBuddyForTransfer(testItem, session, model);

		assertTrue(theView.getViewName().toString().equals("redirect:/transfer")
				&& theView.getModel().containsKey("errorFindingBuddy"));

	}

	@Test
	public void Test_verificationOfExistenceOfBuddyForTransfer() {

		BuddiesInConnexion testItem = new BuddiesInConnexion();
		testItem.setEmail("barack.obama@gmail.com");
		when(transacServices.addingABuddyToTheCurrentUser(testItem, currentUser, session)).thenReturn(true);

		ModelAndView theView = transferController.verificationOfExistenceOfBuddyForTransfer(testItem, session, model);

		assertTrue(theView.getViewName().toString().equals("redirect:/transfer"));

	}

	// =======================================================================================================
	// ================================== TEST FOR OperationController
	// ======================================
	// =======================================================================================================
	
	
	
	@Test
	public void Test_depositSomeMoney_ShouldSendtooMuchMoneyOnYouAccount() {

		BankAccountWithdrawalDepositInformation testItem = new BankAccountWithdrawalDepositInformation();
		testItem.setAmount(150.00);
		
		when(operationOnAccountServices.checkIfCurrentUserCanStillDepositToItsAccount(currentUser,testItem.getAmount())).thenReturn(false);


		ModelAndView theView = operationController.depositSomeMoney(testItem, model, session);

		assertTrue(theView.getViewName().toString().equals("redirect:/userHome") && theView.getModel().containsKey("tooMuchMoneyOnYouAccount"));

	}
	
	
	@Test
	public void Test_withdrawSomeMoney_ShouldSendErrorFlag() {
		
		BankAccountWithdrawalDepositInformation testItem = new BankAccountWithdrawalDepositInformation();
		testItem.setAmount(150.00);

		when(operationOnAccountServices.checkIfCurrentUserHasNecessaryFunds(currentUser, testItem.getAmount())).thenReturn(false);

		ModelAndView theView = operationController.withdrawSomeMoney(testItem, model, session);

		assertTrue(theView.getViewName().toString().equals("redirect:/userHome")  && theView.getModel().containsKey("withdrawErrorFlag"));
		
	}
	
	
	@Test
	public void Test_withdrawSomeMoney() {
		
		BankAccountWithdrawalDepositInformation testItem = new BankAccountWithdrawalDepositInformation();
		testItem.setAmount(150.00);

		when(operationOnAccountServices.checkIfCurrentUserHasNecessaryFunds(currentUser, testItem.getAmount())).thenReturn(true);

		ModelAndView theView = operationController.withdrawSomeMoney(testItem, model, session);
		
		
		verify(operationOnAccountServices, times(1)).saveForDepositorWithdrawal(currentUser, testItem.getAmount(), false, session);


		assertTrue(theView.getViewName().toString().equals("redirect:/userHome"));
		
	}
	
	
	
	@Test
	public void Test_depositSomeMoney_ShouldSendErrorFlag() {
		
		BankAccountWithdrawalDepositInformation testItem = new BankAccountWithdrawalDepositInformation();
		testItem.setAmount(150.00);

		when(operationOnAccountServices.checkIfCurrentUserCanStillDepositToItsAccount(currentUser, testItem.getAmount())).thenReturn(false);

		ModelAndView theView = operationController.depositSomeMoney(testItem, model, session);

		assertTrue(theView.getViewName().toString().equals("redirect:/userHome")  && theView.getModel().containsKey("tooMuchMoneyOnYouAccount"));
		
	}
	
	
	@Test
	public void Test_depositSomeMoney() {
		
		BankAccountWithdrawalDepositInformation testItem = new BankAccountWithdrawalDepositInformation();
		testItem.setAmount(150.00);

		when(operationOnAccountServices.checkIfCurrentUserCanStillDepositToItsAccount(currentUser, testItem.getAmount())).thenReturn(true);

		ModelAndView theView = operationController.depositSomeMoney(testItem, model, session);
		
		
		verify(operationOnAccountServices, times(1)).saveForDepositorWithdrawal(currentUser, testItem.getAmount(), true, session);


		assertTrue(theView.getViewName().toString().equals("redirect:/userHome"));
		
	}
	
	

}

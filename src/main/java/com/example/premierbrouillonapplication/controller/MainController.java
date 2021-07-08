package com.example.premierbrouillonapplication.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import com.example.premierbrouillonapplication.model.BankAccount;
import com.example.premierbrouillonapplication.model.BuddiesInConnexion;
import com.example.premierbrouillonapplication.model.IdentificationData;
import com.example.premierbrouillonapplication.model.LoginRegistration;
import com.example.premierbrouillonapplication.model.PaymentData;
import com.example.premierbrouillonapplication.model.Person;
import com.example.premierbrouillonapplication.model.Transaction;
import com.example.premierbrouillonapplication.model.BankAccountWithdrawalDepositInformation;
import com.example.premierbrouillonapplication.service.BankAccountServices;
import com.example.premierbrouillonapplication.service.MyAppServices;
import com.example.premierbrouillonapplication.service.PersonServices;
import com.example.premierbrouillonapplication.service.TransactionsServices;

@Controller
public class MainController {

	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(MainController.class);

	@Autowired
	MyAppServices services;

	@Autowired
	PersonServices persService;

	@Autowired
	TransactionsServices transacServices;

	@Autowired
	BankAccountServices bankAccountServices;

	private Person currentUser;
	private List<Transaction> listofAllTransaction;
	private Map<Integer, String> listOfBuddies;

	@GetMapping("/")
	public String returnMainPage(Model model) {

		model.addAttribute("comingUser", new IdentificationData());

		return "login";
	}

	@GetMapping("/userHome")
	public String returnHomePage(Model model, HttpSession session) {

		RefreshAndInitializeAllImportantData(session);

		BankAccountWithdrawalDepositInformation depositInfo = new BankAccountWithdrawalDepositInformation();
		BankAccountWithdrawalDepositInformation withdrawalInfo = new BankAccountWithdrawalDepositInformation();

		Double amountAvailable = bankAccountServices.getTheAccountBalance(currentUser.getId());
		model.addAttribute("AmountAvailable", amountAvailable);
		model.addAttribute("depositInformation", depositInfo);
		model.addAttribute("withdrawalInformation", withdrawalInfo);

		return "Home";
	}

	@GetMapping("/profileUser")
	public String returnUserProfil(HttpSession session) {
		RefreshAndInitializeAllImportantData(session);

		return "UserProfile";
	}

	@GetMapping("/contact")
	public String returnContactPages(HttpSession session) {
		RefreshAndInitializeAllImportantData(session);

		return "contact";
	}

	@PostMapping("/process_signin")
	public String verifyIdentity(IdentificationData person, Model model, HttpSession session) {

		currentUser = services.findById(person);

		if (currentUser == null) {
			model.addAttribute("ErrorFlag", true);
			model.addAttribute("comingUser", new IdentificationData());

			return "login";
		}

		listofAllTransaction = currentUser.getAllTransactions();
		listOfBuddies = transacServices.getListNoDuplicates(listofAllTransaction, currentUser);

		model.addAttribute("listTransactions", listofAllTransaction);
		model.addAttribute("listOfBuddies", listOfBuddies);
		PaymentData payId = new PaymentData();
		model.addAttribute("PaymentID", payId);
		model.addAttribute("buddy", new BuddiesInConnexion());
		model.addAttribute("currentUser", currentUser);

		session.setAttribute("listOfBuddies", listOfBuddies);
		session.setAttribute("currentUser", currentUser);
		session.setAttribute("listTransactions", listofAllTransaction);

		return "Transfer";

	}

	@GetMapping("/transfer")
	public String returnTransferPage(Model model, HttpSession session) {
		RefreshAndInitializeAllImportantData(session);

		Person currentUser = (Person) session.getAttribute("currentUser");
		Map<Integer, String> listOfBuddies = (Map<Integer, String>) session.getAttribute("listOfBuddies");
		List<Transaction> listofAllTransaction = (List<Transaction>) session.getAttribute("listTransactions");
		model.addAttribute("buddy", new BuddiesInConnexion());
		model.addAttribute("listOfBuddies", listOfBuddies);
		model.addAttribute("listTransactions", listofAllTransaction);
		PaymentData payId = new PaymentData();
		model.addAttribute("PaymentID", payId);
		model.addAttribute("currentUser", currentUser);

		return "Transfer";
	}

	@PostMapping("/transfer_validation")
	public String verificationOfBuddy(BuddiesInConnexion bud, HttpSession session, Model model) {

		RefreshAndInitializeAllImportantData(session);

		Map<Integer, String> result = persService.checkEmailFromBuddy(bud, currentUser);

		if (!result.isEmpty() && result.get(0) == null) {

			listOfBuddies.putAll(result);
			session.setAttribute("listOfBuddies", listOfBuddies);

			model.addAttribute("buddy", new BuddiesInConnexion());
			model.addAttribute("listOfBuddies", listOfBuddies);
			model.addAttribute("listTransactions", listofAllTransaction);
			PaymentData payId = new PaymentData();
			model.addAttribute("PaymentID", payId);
			model.addAttribute("currentUser", currentUser);

			return "Transfer";

		} else if (result.get(0) != null) {

			model.addAttribute("CannotAddYourself", true);

		} else {

			model.addAttribute("ErrorFindingBuddy", true);
		}

		model.addAttribute("buddy", new BuddiesInConnexion());
		model.addAttribute("listOfBuddies", listOfBuddies);
		model.addAttribute("listTransactions", listofAllTransaction);
		PaymentData payId = new PaymentData();
		model.addAttribute("PaymentID", payId);
		model.addAttribute("currentUser", currentUser);

		return "Transfer";

	}

	@PostMapping("/withDrawPayment")
	public String withdrawSomeMoney(BankAccountWithdrawalDepositInformation withdrawMoney, Model model,
			HttpSession session) {

		RefreshAndInitializeAllImportantData(session);

		BankAccountWithdrawalDepositInformation depositInfo = new BankAccountWithdrawalDepositInformation();
		BankAccountWithdrawalDepositInformation withdrawalInfo = new BankAccountWithdrawalDepositInformation();

		if (bankAccountServices.checkAmounts(currentUser, withdrawMoney.getAmount())) {

			bankAccountServices.saveForDepositorWithdrawal(currentUser, -withdrawMoney.getAmount());

		} else {

			model.addAttribute("WithdrawErrorFlag", true);
		}

		model.addAttribute("AmountAvailable", bankAccountServices.findById(currentUser.getId()).getAmount());
		model.addAttribute("depositInformation", depositInfo);
		model.addAttribute("withdrawalInformation", withdrawalInfo);

		return "Home";
	}

	@PostMapping("/DepositAmount")
	public String depositSomeMoney(BankAccountWithdrawalDepositInformation depositMoney, Model model,
			HttpSession session) {

		RefreshAndInitializeAllImportantData(session);

		BankAccountWithdrawalDepositInformation depositInfo = new BankAccountWithdrawalDepositInformation();
		BankAccountWithdrawalDepositInformation withdrawalInfo = new BankAccountWithdrawalDepositInformation();

		bankAccountServices.saveForDepositorWithdrawal(currentUser, depositMoney.getAmount());

		model.addAttribute("AmountAvailable", bankAccountServices.findById(currentUser.getId()).getAmount());
		model.addAttribute("depositInformation", depositInfo);
		model.addAttribute("withdrawalInformation", withdrawalInfo);

		return "Home";
	}

	@GetMapping("/register")
	public String registeringNewPerson(Model model, HttpSession session) {

		model.addAttribute("newUser", new LoginRegistration());

		return "Register";
	}

	@PostMapping("/process_register")
	public String processRegistration(LoginRegistration person, HttpSession session, Model model) {

		if (persService.checkExistingMail(person)) {

			model.addAttribute("existingmail", true);
			model.addAttribute("newUser", new LoginRegistration());

			return "Register";

		}

		if (!person.getPassword().equals(person.getSecondTestPassword())) {

			model.addAttribute("passwordmatch", true);
			model.addAttribute("newUser", new LoginRegistration());

			return "Register";

		}

		persService.saveNewPerson(person);

		return "SucessfullyRegistered";

	}

	@PostMapping("/processingPayment")
	public String processPayment(PaymentData pay, Model model, HttpSession session) {

		RefreshAndInitializeAllImportantData(session);

		logger.info("----------------------------------------- " + currentUser.getId());
		logger.info("------------XXXXXXXXXXXXXXXXXXX--------------- "
				+ persService.getIt(Integer.parseInt(pay.getPersonToPay())).getId());
		logger.info("------------)))))))))))))))((((((((((((((--------------- " + pay.getAmount() + "!!!");

		BankAccount personToPay = bankAccountServices.findById(Integer.parseInt(pay.getPersonToPay()));

		if (bankAccountServices.checkAmounts(currentUser, pay.getAmount() * 1.005) && personToPay != null) {

			bankAccountServices.adjustAccount(persService.getIt(Integer.parseInt(pay.getPersonToPay())), currentUser,
					pay.getAmount());
			transacServices.saveANewTransaction(currentUser, pay,
					persService.getIt(Integer.parseInt(pay.getPersonToPay())));

			logger.info("Is it working ================================ ? : " + pay.getAmount() * 1.005);
			RefreshAndInitializeAllImportantData(session);
			model.addAttribute("buddy", new BuddiesInConnexion());
			model.addAttribute("listOfBuddies", listOfBuddies);
			model.addAttribute("listTransactions", listofAllTransaction);
			PaymentData payId = new PaymentData();
			model.addAttribute("PaymentID", payId);
			model.addAttribute("currentUser", currentUser);

			return "Transfer";

		} else if (personToPay == null) {

			model.addAttribute("ErrorInitializationAccountBuddy", true);

		} else {

			model.addAttribute("ErrorPayingBuddy", true);
		}

		model.addAttribute("buddy", new BuddiesInConnexion());
		model.addAttribute("listOfBuddies", listOfBuddies);
		model.addAttribute("listTransactions", listofAllTransaction);
		PaymentData payId = new PaymentData();
		model.addAttribute("PaymentID", payId);
		model.addAttribute("currentUser", currentUser);

		return "Transfer";

	}

	private void RefreshAndInitializeAllImportantData(HttpSession session) {

		currentUser = (Person) session.getAttribute("currentUser");

		listofAllTransaction = currentUser.getAllTransactions();
		listOfBuddies = transacServices.getListNoDuplicates(listofAllTransaction, currentUser);

	}

}

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
import com.example.premierbrouillonapplication.model.DepositInformation;
import com.example.premierbrouillonapplication.model.IdentificationData;
import com.example.premierbrouillonapplication.model.LoginRegistration;
import com.example.premierbrouillonapplication.model.PaymentData;
import com.example.premierbrouillonapplication.model.Person;
import com.example.premierbrouillonapplication.model.Transaction;
import com.example.premierbrouillonapplication.model.WithdrawalInformation;
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

	@GetMapping("/")
	public String returnMainPage(Model model) {

		model.addAttribute("comingUser", new IdentificationData());

		return "login";
	}

	@GetMapping("/userHome")
	public String returnHomePage(Model model, HttpSession session) {

		Person currentUser = (Person) session.getAttribute("Dude");

		DepositInformation depositInfo = new DepositInformation();
		WithdrawalInformation withdrawalInfo = new WithdrawalInformation();

		Double amountAvailable = bankAccountServices.getTheAccountBalance(currentUser.getId());
		model.addAttribute("AmountAvailable", amountAvailable);
		model.addAttribute("depositInformation", depositInfo);
		model.addAttribute("withdrawalInformation", withdrawalInfo);

		return "Home";
	}

	@GetMapping("/profileUser")
	public String returnUserProfil() {

		return "UserProfile";
	}

	@GetMapping("/contact")
	public String returnContactPages() {
		return "contact";
	}

	@GetMapping("/logoff")
	public String returnLoginOff(SessionStatus status) {

		status.setComplete();

		return "LogOff";
	}

	@PostMapping("/process_signin")
	public String verifyIdentity(IdentificationData person, Model model, HttpSession session) {

		logger.info("What is happening ?" + person.getPassword() + person.getEmail());
		Person testing = services.findById(person);

		if (testing == null) {
			return "no_found_V1";
		}

		List<Transaction> listofAllTransaction = new ArrayList<>();
		listofAllTransaction = testing.getTransactions();

		Map<Integer, String> listOfBuddies = transacServices.getListNoDuplicates(listofAllTransaction);

		model.addAttribute("listTransactions", listofAllTransaction);
		model.addAttribute("listOfBuddies", listOfBuddies);
		PaymentData payId = new PaymentData();
		model.addAttribute("PaymentID", payId);
		model.addAttribute("buddy", new BuddiesInConnexion());
		session.setAttribute("listOfBuddies", listOfBuddies);
		session.setAttribute("Dude", testing);
		session.setAttribute("listTransactions", listofAllTransaction);

		return "Transfer";

	}

	@GetMapping("/transfer")
	public String returnTransferPage(Model model, HttpSession session) {

		Person currentUser = (Person) session.getAttribute("Dude");
		Map<Integer, String> listOfBuddies = (Map<Integer, String>) session.getAttribute("listOfBuddies");
		List<Transaction> listofAllTransaction = (List<Transaction>) session.getAttribute("listTransactions");
		model.addAttribute("buddy", new BuddiesInConnexion());
		model.addAttribute("listOfBuddies", listOfBuddies);
		model.addAttribute("listTransactions", listofAllTransaction);
		PaymentData payId = new PaymentData();
		model.addAttribute("PaymentID", payId);

		return "Transfer";
	}

	@PostMapping("/transfer_validation")
	public String verificationOfBuddy(BuddiesInConnexion bud, HttpSession session, Model model) {

		Map<Integer, String> result = persService.checkEmailFromBuddy(bud);

		if (!result.isEmpty()) {
			Map<Integer, String> listOfBuddies = (Map<Integer, String>) session.getAttribute("listOfBuddies");
			listOfBuddies.putAll(result);
			session.setAttribute("listOfBuddies", listOfBuddies);

			Person currentUser = (Person) session.getAttribute("Dude");
			List<Transaction> listofAllTransaction = (List<Transaction>) session.getAttribute("listTransactions");
			model.addAttribute("buddy", new BuddiesInConnexion());
			model.addAttribute("listOfBuddies", listOfBuddies);
			model.addAttribute("listTransactions", listofAllTransaction);
			PaymentData payId = new PaymentData();
			model.addAttribute("PaymentID", payId);

			return "Transfer";

		}

		Person currentUser = (Person) session.getAttribute("Dude");
		Map<Integer, String> listOfBuddies = (Map<Integer, String>) session.getAttribute("listOfBuddies");
		List<Transaction> listofAllTransaction = (List<Transaction>) session.getAttribute("listTransactions");
		model.addAttribute("buddy", new BuddiesInConnexion());
		model.addAttribute("listOfBuddies", listOfBuddies);
		model.addAttribute("listTransactions", listofAllTransaction);
		PaymentData payId = new PaymentData();
		model.addAttribute("PaymentID", payId);

		return "Transfer";

	}

	@PostMapping("/withDrawPayment")
	public String withdrawSomeMoney(WithdrawalInformation withdraw) {
		logger.info("What is happening in wiiiithhddrawwalll?" + withdraw.getAmount());
		return "Home";
	}

	@PostMapping("/DepositAmount")
	public String depositSomeMoney(DepositInformation depositMoney) {

		logger.info("What is happening IN DEPOOOOOOSIT?" + depositMoney.getAccountNumber() + "//"
				+ depositMoney.getAmountToDeposit());
		return "Home";
	}

	@GetMapping("/register")
	public String registeringNewPerson(Model model) {

		model.addAttribute("newUser", new LoginRegistration());

		return "Register";
	}

	@PostMapping("/process_register")
	public String processRegistration(LoginRegistration person) {

		logger.info("Is it working ? : " + person.geteMail() + " / " + person.getLastName() + " / " + person.getName()
				+ "///" + person.getPassword());

		persService.saveNewPerson(person);

		return "SucessfullyRegistered";

	}

	@PostMapping("/processingPayment")
	public String processPayment(PaymentData pay, Model model, HttpSession session) {

		Person currentUser = (Person) session.getAttribute("Dude");
		if (bankAccountServices.checkAvailability(persService.getIt(Integer.parseInt(pay.getPersonToPay())),
				currentUser, pay.getAmount())) {

			logger.info("===========================RESULTS : "
					+ persService.getIt(Integer.parseInt(pay.getPersonToPay())).getName());

			bankAccountServices.adjustAccount(persService.getIt(Integer.parseInt(pay.getPersonToPay())), currentUser,
					pay.getAmount());
			transacServices.saveANewTransaction(currentUser, pay,
					persService.getIt(Integer.parseInt(pay.getPersonToPay())));
			return "processingAccepted";

		} else {

			return null;
		}
	}

}

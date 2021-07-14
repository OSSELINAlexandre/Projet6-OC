package com.example.paymybuddy.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.paymybuddy.DTO.BuddiesInConnexion;
import com.example.paymybuddy.DTO.PaymentData;
import com.example.paymybuddy.model.BankOperation;
import com.example.paymybuddy.model.ConnexionBetweenBuddies;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.model.Transaction;
import com.example.paymybuddy.constant.*;
import com.example.paymybuddy.service.TransactionsServices;

@Controller
public class TransferController {
	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TransferController.class);

	@Autowired
	private TransactionsServices transactionServices;

	private Person currentUser;
	private List<Transaction> listOfAllTransactions;
	private Map<Person, String> ListOfBuddies;

	@GetMapping("/transfer")
	public String returnTransferPage(@RequestParam("ErrorFindingBuddy") Optional<Boolean> ErrorFindingBuddy,
			@RequestParam("ErrorPayingBuddy") Optional<Boolean> ErrorPayingBuddy,
			@RequestParam("AlreadyHavingBuddy") Optional<Boolean> AlreadyHavingBuddy,
			@RequestParam("cannotAddYourself") Optional<Boolean> cannotAddYourself, Model model, HttpSession session) {

		refreshAndInitializeAllImportantData(session);

		Person currentUser = (Person) session.getAttribute("currentUser");
		model.addAttribute("buddy", new BuddiesInConnexion());
		model.addAttribute("listOfBuddies", ListOfBuddies);
		model.addAttribute("listTransactions", listOfAllTransactions);
		PaymentData payId = new PaymentData();
		model.addAttribute("paymentID", payId);
		model.addAttribute("currentUser", currentUser);

		if (ErrorFindingBuddy.isPresent()) {

			model.addAttribute("ErrorFindingBuddy", true);
		}
		if (ErrorPayingBuddy.isPresent()) {

			model.addAttribute("ErrorPayingBuddy", true);

		}
		if (AlreadyHavingBuddy.isPresent()) {

			model.addAttribute("AlreadyHavingBuddy", true);

		}
		if (cannotAddYourself.isPresent()) {

			model.addAttribute("cannotAddYourself", true);

		}

		return "transfer_page";
	}

	@PostMapping("/transfer_validation")
	public ModelAndView verificationOfBuddy(BuddiesInConnexion bud, HttpSession session, Model model,
			ModelMap modelmap) {

		refreshAndInitializeAllImportantData(session);
		ModelAndView theView = new ModelAndView("redirect:http://localhost:8080/transfer");
		ConnexionBetweenBuddies temporaryConnexion = transactionServices.addingABuddyToTheCurrentUser(bud, currentUser);

		if (transactionServices.checkIfBuddyExists(bud) && temporaryConnexion != null) {

			Map<Person, String> tempResult = transactionServices.addTemporaryConnexion(temporaryConnexion);
			ListOfBuddies.putAll(tempResult);

		} else if (transactionServices.checkIfBuddyExists(bud) && temporaryConnexion == null) {

			if (transactionServices.findByEmailFromRepo(bud.getEmail()).getId() == currentUser.getId()) {

				theView.addObject("cannotAddYourself", true);

			} else {

				theView.addObject("AlreadyHavingBuddy", true);
			}

		} else {

			theView.addObject("ErrorFindingBuddy", true);

		}

		return theView;

	}

	@PostMapping("/processingPayment")
	public ModelAndView processPayment(PaymentData pay, Model model, HttpSession session, ModelMap modelmap) {

		refreshAndInitializeAllImportantData(session);
		ModelAndView theView = new ModelAndView("redirect:http://localhost:8080/transfer");

		if (transactionServices.checkAmounts(currentUser, pay.getAmount() * (1 + Fees.CLASSIC_FEE_APP))) {

			Transaction tempTransac = transactionServices.adjustAccount(pay, currentUser);

			currentUser.setTransactionsPayed(transactionServices.setNewTransactionPayed(currentUser, tempTransac));

		} else {

			theView.addObject("ErrorPayingBuddy", true);
		}

		return theView;

	}

	private void refreshAndInitializeAllImportantData(HttpSession session) {

		currentUser = (Person) session.getAttribute("currentUser");
		logger.info("--------------------- LOL -------------------" + currentUser.getId());
		ListOfBuddies = (Map<Person, String>) session.getAttribute("listOfBuddies");
		listOfAllTransactions = currentUser.getAllTransactions();

	}

}

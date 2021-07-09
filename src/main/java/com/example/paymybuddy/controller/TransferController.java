package com.example.paymybuddy.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.paymybuddy.DTO.BuddiesInConnexion;
import com.example.paymybuddy.DTO.PaymentData;
import com.example.paymybuddy.model.BankOperation;
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
	private Map<Integer, String> listOfBuddies;

	@PostMapping("/transfer_validation")
	public String verificationOfBuddy(BuddiesInConnexion bud, HttpSession session, Model model) {

		RefreshAndInitializeAllImportantData(session);

		Map<Integer, String> result = transactionServices.checkEmailFromBuddy(bud, currentUser);

		if (!result.isEmpty() && result.get(0) == null) {

			listOfBuddies.putAll(result);
			session.setAttribute("listOfBuddies", listOfBuddies);

			model.addAttribute("buddy", new BuddiesInConnexion());
			model.addAttribute("listOfBuddies", listOfBuddies);
			model.addAttribute("listTransactions", listOfAllTransactions);
			PaymentData payId = new PaymentData();
			model.addAttribute("paymentID", payId);
			model.addAttribute("currentUser", currentUser);

			return "transfer_page";

		} else if (result.get(0) != null) {

			model.addAttribute("CannotAddYourself", true);

		} else {

			model.addAttribute("ErrorFindingBuddy", true);
		}

		model.addAttribute("buddy", new BuddiesInConnexion());
		model.addAttribute("listOfBuddies", listOfBuddies);
		model.addAttribute("listTransactions", listOfAllTransactions);
		PaymentData payId = new PaymentData();
		model.addAttribute("paymentID", payId);
		model.addAttribute("currentUser", currentUser);

		return "transfer_page";

	}

	@PostMapping("/processingPayment")
	public String processPayment(PaymentData pay, Model model, HttpSession session) {

		RefreshAndInitializeAllImportantData(session);

		BankOperation personToPay = transactionServices.findBankOperationById(Integer.parseInt(pay.getPersonToPay()));

		if (transactionServices.checkAmounts(currentUser, pay.getAmount() * (1 + Fees.CLASSIC_FEE_APP))
				&& personToPay != null) {

			transactionServices.adjustAccount(transactionServices.getPersonById(Integer.parseInt(pay.getPersonToPay())),
					currentUser, pay.getAmount());
			transactionServices.saveANewTransaction(currentUser, pay,
					transactionServices.getPersonById(Integer.parseInt(pay.getPersonToPay())));

			RefreshAndInitializeAllImportantData(session);
			model.addAttribute("buddy", new BuddiesInConnexion());
			model.addAttribute("listOfBuddies", listOfBuddies);
			model.addAttribute("listTransactions", listOfAllTransactions);
			PaymentData payId = new PaymentData();
			model.addAttribute("paymentID", payId);
			model.addAttribute("currentUser", currentUser);

			return "transfer_page";

		} else if (personToPay == null) {

			model.addAttribute("ErrorInitializationAccountBuddy", true);

		} else {

			model.addAttribute("ErrorPayingBuddy", true);
		}

		model.addAttribute("buddy", new BuddiesInConnexion());
		model.addAttribute("listOfBuddies", listOfBuddies);
		model.addAttribute("listTransactions", listOfAllTransactions);
		PaymentData payId = new PaymentData();
		model.addAttribute("paymentID", payId);
		model.addAttribute("currentUser", currentUser);

		return "transfer_page";

	}

	private void RefreshAndInitializeAllImportantData(HttpSession session) {

		currentUser = (Person) session.getAttribute("currentUser");

		listOfAllTransactions = currentUser.getAllTransactions();
		listOfBuddies = transactionServices.getListNoDuplicates(listOfAllTransactions, currentUser);

	}

}

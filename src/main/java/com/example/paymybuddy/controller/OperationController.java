package com.example.paymybuddy.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.paymybuddy.DTO.BankAccountWithdrawalDepositInformation;
import com.example.paymybuddy.model.BankOperation;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.model.Transaction;
import com.example.paymybuddy.service.OperationOnAccountServices;

@Controller
public class OperationController {

	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(OperationController.class);

	@Autowired
	OperationOnAccountServices bankAccountServices;

	private Person currentUser;
	private List<BankOperation> listOfAllOperation;

	@PostMapping("/withDrawPayment")
	public String withdrawSomeMoney(BankAccountWithdrawalDepositInformation withdrawMoney, Model model,
			HttpSession session) {

		RefreshAndInitializeAllImportantData(session);

		BankAccountWithdrawalDepositInformation depositInfo = new BankAccountWithdrawalDepositInformation();
		BankAccountWithdrawalDepositInformation withdrawalInfo = new BankAccountWithdrawalDepositInformation();
		List<BankOperation> listOfAllOperations = (List<BankOperation>) session.getAttribute("listOfAllOperations");

		if (bankAccountServices.checkAmounts(currentUser, withdrawMoney.getAmount())) {

			BankOperation transitoryItem = bankAccountServices.saveForDepositorWithdrawal(currentUser,
					withdrawMoney.getAmount(), false);
			listOfAllOperations.add(transitoryItem);
		} else {

			model.addAttribute("withdrawErrorFlag", true);
		}

		model.addAttribute("amountAvailable", bankAccountServices.findById(currentUser.getId()).getAmount());
		logger.info("----------------------------------------------------------------"
				+ bankAccountServices.findById(currentUser.getId()).getAmount());

		model.addAttribute("listOperations", listOfAllOperations);
		model.addAttribute("depositInformation", depositInfo);
		model.addAttribute("withdrawalInformation", withdrawalInfo);

		return "home_page";
	}

	@PostMapping("/DepositAmount")
	public String depositSomeMoney(BankAccountWithdrawalDepositInformation depositMoney, Model model,
			HttpSession session) {

		RefreshAndInitializeAllImportantData(session);

		BankAccountWithdrawalDepositInformation depositInfo = new BankAccountWithdrawalDepositInformation();
		BankAccountWithdrawalDepositInformation withdrawalInfo = new BankAccountWithdrawalDepositInformation();

		BankOperation transitoryItem = bankAccountServices.saveForDepositorWithdrawal(currentUser,
				depositMoney.getAmount(), true);

		model.addAttribute("amountAvailable", bankAccountServices.findById(currentUser.getId()).getAmount());
		List<BankOperation> listOfAllOperations = (List<BankOperation>) session.getAttribute("listOfAllOperations");
		listOfAllOperations.add(transitoryItem);
		model.addAttribute("listOperations", listOfAllOperations);
		model.addAttribute("depositInformation", depositInfo);
		model.addAttribute("withdrawalInformation", withdrawalInfo);

		return "home_page";
	}

	private void RefreshAndInitializeAllImportantData(HttpSession session) {

		currentUser = (Person) session.getAttribute("currentUser");

	}

}

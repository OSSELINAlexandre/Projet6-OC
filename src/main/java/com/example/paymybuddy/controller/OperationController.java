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

	// This attribute is to avoid refresh error : multiple withdraw can be done
	// if the user refresh the page after a withdraw.
	public static Boolean refreshErrorTrueIfAlreadyBeingPaidWithDraw;

	// This attribute is to avoid refresh error : multiple withdraw can be done
	// if the user refresh the page after a deposit.
	public static Boolean refreshErrorTrueIfAlreadyBeingPaidDeposit;

	@PostMapping("/withDrawPayment")
	public String withdrawSomeMoney(BankAccountWithdrawalDepositInformation withdrawMoney, Model model,
			HttpSession session) {
		refreshErrorTrueIfAlreadyBeingPaidDeposit = true;

		refreshAndInitializeAllImportantData(session);

		BankAccountWithdrawalDepositInformation depositInfo = new BankAccountWithdrawalDepositInformation();
		BankAccountWithdrawalDepositInformation withdrawalInfo = new BankAccountWithdrawalDepositInformation();
		List<BankOperation> listOfAllOperations = (List<BankOperation>) session.getAttribute("listOfAllOperations");

		if (bankAccountServices.checkAmounts(currentUser, withdrawMoney.getAmount())
				&& refreshErrorTrueIfAlreadyBeingPaidWithDraw) {
			refreshErrorTrueIfAlreadyBeingPaidWithDraw = false;
			BankOperation transitoryItem = bankAccountServices.saveForDepositorWithdrawal(currentUser,
					withdrawMoney.getAmount(), false);
			listOfAllOperations.add(transitoryItem);
		} else if (!bankAccountServices.checkAmounts(currentUser, withdrawMoney.getAmount())
				&& refreshErrorTrueIfAlreadyBeingPaidWithDraw) {

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
		refreshErrorTrueIfAlreadyBeingPaidWithDraw = true;
		refreshAndInitializeAllImportantData(session);

		BankAccountWithdrawalDepositInformation depositInfo = new BankAccountWithdrawalDepositInformation();
		BankAccountWithdrawalDepositInformation withdrawalInfo = new BankAccountWithdrawalDepositInformation();

		if (refreshErrorTrueIfAlreadyBeingPaidDeposit) {
			BankOperation transitoryItem = bankAccountServices.saveForDepositorWithdrawal(currentUser,
					depositMoney.getAmount(), true);
			List<BankOperation> listOfAllOperations = (List<BankOperation>) session.getAttribute("listOfAllOperations");
			listOfAllOperations.add(transitoryItem);
			refreshErrorTrueIfAlreadyBeingPaidDeposit = false;
		}

		model.addAttribute("amountAvailable", bankAccountServices.findById(currentUser.getId()).getAmount());
		List<BankOperation> listOfAllOperations = (List<BankOperation>) session.getAttribute("listOfAllOperations");
		model.addAttribute("listOperations", listOfAllOperations);
		model.addAttribute("depositInformation", depositInfo);
		model.addAttribute("withdrawalInformation", withdrawalInfo);

		return "home_page";
	}

	private void refreshAndInitializeAllImportantData(HttpSession session) {

		currentUser = (Person) session.getAttribute("currentUser");

	}

}

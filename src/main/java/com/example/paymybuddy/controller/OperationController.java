package com.example.paymybuddy.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.paymybuddy.DTO.BankAccountWithdrawalDepositInformation;
import com.example.paymybuddy.model.BankOperation;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.service.OperationOnAccountServices;
import com.example.paymybuddy.service.UserServices;

@Controller
public class OperationController {

	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(OperationController.class);

	@Autowired
	OperationOnAccountServices bankAccountServices;

	@Autowired
	UserServices userServices;

	private Person currentUser;

	@GetMapping("/userHome")
	public String returnHomePage(@RequestParam("withdrawErrorFlag") Optional<Boolean> WithdrawError, Model model,
			HttpSession session) {

		refreshAndInitializeAllImportantData(session);

		BankAccountWithdrawalDepositInformation depositInfo = new BankAccountWithdrawalDepositInformation();
		BankAccountWithdrawalDepositInformation withdrawalInfo = new BankAccountWithdrawalDepositInformation();
		List<BankOperation> listOfAllOperations = (List<BankOperation>) session.getAttribute("listOfAllOperations");

		Double amountAvailable = userServices.getTheAccountBalance(currentUser.getId());
		model.addAttribute("amountAvailable", amountAvailable);
		model.addAttribute("depositInformation", depositInfo);
		model.addAttribute("withdrawalInformation", withdrawalInfo);
		model.addAttribute("listOperations", listOfAllOperations);

		if (WithdrawError.isPresent()) {
			model.addAttribute("withdrawErrorFlag", WithdrawError);
		}

		return "home_page";
	}

	@PostMapping("/withDrawPayment")
	public ModelAndView withdrawSomeMoney(BankAccountWithdrawalDepositInformation withdrawMoney, Model model,
			HttpSession session, ModelMap modelmap) {

		ModelAndView theview = new ModelAndView("redirect:http://localhost:8080/userHome");

		refreshAndInitializeAllImportantData(session);

		if (bankAccountServices.checkAmounts(currentUser, withdrawMoney.getAmount())) {

			BankOperation transitoryItem = bankAccountServices.saveForDepositorWithdrawal(currentUser,
					withdrawMoney.getAmount(), false);
			bankAccountServices.setTemporaryList(transitoryItem, session);
			
			
		} else {

			theview.addObject("withdrawErrorFlag", true);
		}

		return theview;
	}

	@PostMapping("/DepositAmount")
	public ModelAndView depositSomeMoney(BankAccountWithdrawalDepositInformation depositMoney, Model model,
			HttpSession session) {
		refreshAndInitializeAllImportantData(session);

		ModelAndView theview = new ModelAndView("redirect:http://localhost:8080/userHome");

		BankOperation transitoryItem = bankAccountServices.saveForDepositorWithdrawal(currentUser,
				depositMoney.getAmount(), true);
		bankAccountServices.setTemporaryList(transitoryItem, session);

		return theview;
	}

	private void refreshAndInitializeAllImportantData(HttpSession session) {

		currentUser = (Person) session.getAttribute("currentUser");

	}

}

package com.example.paymybuddy.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

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

	@Autowired
	OperationOnAccountServices bankAccountServices;

	@Autowired
	UserServices userServices;

	private Person currentUser;
	private List<BankOperation> listOfAllOperations;

	@GetMapping("/userHome")
	public String returnHomePage(@RequestParam("withdrawErrorFlag") Optional<Boolean> withdrawError, 
			@RequestParam("tooMuchMoneyOnYouAccount") Optional<Boolean> tooMuchMoney, Model model,
			HttpSession session) {

		listOfAllOperations = (List<BankOperation>) session.getAttribute("listOfAllOperations");
		currentUser = (Person) session.getAttribute("currentUser");

		model.addAttribute("amountAvailable", currentUser.getAmount());
		model.addAttribute("depositInformation", new BankAccountWithdrawalDepositInformation());
		model.addAttribute("withdrawalInformation", new BankAccountWithdrawalDepositInformation());
		model.addAttribute("listOfAllOperations", listOfAllOperations);

		if (withdrawError.isPresent()) {
			model.addAttribute("withdrawErrorFlag", withdrawError);
		}
		if(tooMuchMoney.isPresent()) {
			
			model.addAttribute("tooMuchMoney", tooMuchMoney);
		}

		return "home_page";
	}

	@PostMapping("/withDrawPayment")
	public ModelAndView withdrawSomeMoney(BankAccountWithdrawalDepositInformation withdrawMoney, Model model,
			HttpSession session, ModelMap modelmap) {

		ModelAndView theview = new ModelAndView("redirect:/userHome");

		if (bankAccountServices.checkIfCurrentUserHasNecessaryFunds(currentUser, withdrawMoney.getAmount())) {

			bankAccountServices.saveForDepositorWithdrawal(currentUser, withdrawMoney.getAmount(), false, session);

		} else {

			theview.addObject("withdrawErrorFlag", true);
		}

		return theview;
	}

	@PostMapping("/DepositAmount")
	public ModelAndView depositSomeMoney(BankAccountWithdrawalDepositInformation depositMoney, Model model,
			HttpSession session) {

		ModelAndView theview = new ModelAndView("redirect:/userHome");

		if (bankAccountServices.checkIfCurrentUserCanStillDepositToItsAccount(currentUser, depositMoney.getAmount())) {

			bankAccountServices.saveForDepositorWithdrawal(currentUser, depositMoney.getAmount(), true, session);

		} else {

			theview.addObject("tooMuchMoneyOnYouAccount", true);
		}

		return theview;
	}

}

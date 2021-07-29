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

import com.example.paymybuddy.dto.BankAccountWithdrawalDepositInformation;
import com.example.paymybuddy.dto.BankInformation;
import com.example.paymybuddy.model.BankOperation;
import com.example.paymybuddy.model.ExternalBankAccount;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.service.OperationOnAccountServices;
import com.example.paymybuddy.service.UserServices;

@Controller
public class OperationController {

	@Autowired
	OperationOnAccountServices bankAccountServices;

	private Person currentUser;
	private List<BankOperation> listOfAllOperations;
	private List<ExternalBankAccount> listOfAllBankAccountOwned;

	
	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(OperationController.class);

	
	@GetMapping("/userHome")
	public String returnHomePage(@RequestParam("withdrawErrorFlag") Optional<Boolean> withdrawError,
			@RequestParam("tooMuchMoneyOnYouAccount") Optional<Boolean> tooMuchMoney, 
			@RequestParam("alreadyAddedBank") Optional<Boolean> alreadyAddedBank,
			@RequestParam("noBankYet") Optional<Boolean> noBankYet,
			Model model,
			HttpSession session) {

		listOfAllOperations = (List<BankOperation>) session.getAttribute("listOfAllOperations");
		currentUser = (Person) session.getAttribute("currentUser");
		listOfAllBankAccountOwned = (List<ExternalBankAccount>) session.getAttribute("listOfAllBankAccountOwned");
		
		
		model.addAttribute("amountAvailable", currentUser.getAccountFunds());
		model.addAttribute("depositInformation", new BankAccountWithdrawalDepositInformation());
		model.addAttribute("withdrawalInformation", new BankAccountWithdrawalDepositInformation());
		model.addAttribute("newBankInformation", new BankInformation());
		model.addAttribute("listOfAllOperations", listOfAllOperations);
		model.addAttribute("listOfAllBankAccountOwned", bankAccountServices.createTheListForUIOperation(listOfAllBankAccountOwned));
		logger.info("listOfAllBankAccountOwned WAS SEND WITH " + listOfAllBankAccountOwned.size());

		if (withdrawError.isPresent()) {
			model.addAttribute("withdrawErrorFlag", withdrawError);
		}
		if (tooMuchMoney.isPresent()) {

			model.addAttribute("tooMuchMoney", tooMuchMoney);
		}
		if(alreadyAddedBank.isPresent()) {
			
			model.addAttribute("alreadyAddedBank", alreadyAddedBank);
		}
		if(noBankYet.isPresent()) {
			
			model.addAttribute("noBankYet", noBankYet);
		}

		return "home_page";
	}

	@PostMapping("/withDrawPayment")
	public ModelAndView withdrawSomeMoney(BankAccountWithdrawalDepositInformation withdrawMoney, Model model,
			HttpSession session) {

		ModelAndView theview = new ModelAndView("redirect:/userHome");
		

		if (bankAccountServices.checkIfCurrentUserHasNecessaryFunds(currentUser, withdrawMoney.getAmount())) {

			bankAccountServices.saveForDepositorWithdrawal(currentUser, withdrawMoney, false, session);

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

			bankAccountServices.saveForDepositorWithdrawal(currentUser, depositMoney, true, session);

		} else {

			theview.addObject("tooMuchMoneyOnYouAccount", true);
		}

		return theview;
	}
	
	
	@PostMapping("/registerNewBank")
	public ModelAndView registerANewBank(BankInformation bankInformation, Model model, HttpSession session) {
		
		ModelAndView theView = new ModelAndView("redirect:/userHome");

		if(bankAccountServices.checkIfCurrentBankExistsInTheAttribute(currentUser, bankInformation)) {
			
			theView.addObject("alreadyAddedBank", true);

		}else {
			
			bankAccountServices.saveANewBankForCurrentUser(currentUser, bankInformation, session);
		}
		
		
		return theView;
		
	}

	///////////////////////////////////////////////////// SETTER FOR TESTING
	///////////////////////////////////////////////////// PURPOSES, CAN BE DELETED
	///////////////////////////////////////////////////// AFTERWARDS
	///////////////////////////////////////////////////// //////////////////////////////

	public void setBankAccountServices(OperationOnAccountServices bankAccountServices) {
		this.bankAccountServices = bankAccountServices;
	}

	public void setCurrentUser(Person currentUser) {
		this.currentUser = currentUser;
	}

	public void setListOfAllOperations(List<BankOperation> listOfAllOperations) {
		this.listOfAllOperations = listOfAllOperations;
	}

}

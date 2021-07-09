package com.example.paymybuddy.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.paymybuddy.DTO.BankAccountWithdrawalDepositInformation;
import com.example.paymybuddy.DTO.BuddiesInConnexion;
import com.example.paymybuddy.DTO.IdentificationData;
import com.example.paymybuddy.DTO.LoginRegistration;
import com.example.paymybuddy.DTO.PaymentData;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.model.Transaction;
import com.example.paymybuddy.service.UserServices;

@Controller
public class UserController {
	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	UserServices userServices;

	private Person currentUser;
	private List<Transaction> listOfAllTransactions;
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

		Double amountAvailable = userServices.getTheAccountBalance(currentUser.getId());
		model.addAttribute("amountAvailable", amountAvailable);
		model.addAttribute("depositInformation", depositInfo);
		model.addAttribute("withdrawalInformation", withdrawalInfo);

		return "home_page";
	}

	@GetMapping("/profileUser")
	public String returnUserProfil(HttpSession session) {
		RefreshAndInitializeAllImportantData(session);

		return "user_profile";
	}

	@GetMapping("/contact")
	public String returnContactPages(HttpSession session) {
		RefreshAndInitializeAllImportantData(session);

		return "contact";
	}

	@PostMapping("/process_signin")
	public String verifyIdentity(IdentificationData person, Model model, HttpSession session) {

		currentUser = userServices.findById(person);

		if (currentUser == null) {
			model.addAttribute("errorFlag", true);
			model.addAttribute("comingUser", new IdentificationData());

			return "login";
		}

		listOfAllTransactions = currentUser.getAllTransactions();
		listOfBuddies = userServices.getListNoDuplicates(listOfAllTransactions, currentUser);

		model.addAttribute("listTransactions", listOfAllTransactions);
		model.addAttribute("listOfBuddies", listOfBuddies);
		PaymentData payId = new PaymentData();
		model.addAttribute("paymentID", payId);
		model.addAttribute("buddy", new BuddiesInConnexion());
		model.addAttribute("currentUser", currentUser);

		session.setAttribute("listOfBuddies", listOfBuddies);
		session.setAttribute("currentUser", currentUser);
		session.setAttribute("listTransactions", listOfAllTransactions);

		return "transfer_page";

	}

	@GetMapping("/transfer")
	public String returnTransferPage(Model model, HttpSession session) {
		RefreshAndInitializeAllImportantData(session);

		Person currentUser = (Person) session.getAttribute("currentUser");
		//TODO add a button to see through a modal all the transaction done.
		List<Transaction> listofAllTransaction = (List<Transaction>) session.getAttribute("listTransactions");
		model.addAttribute("buddy", new BuddiesInConnexion());
		model.addAttribute("listOfBuddies", listOfBuddies);
		model.addAttribute("listTransactions", listofAllTransaction);
		PaymentData payId = new PaymentData();
		model.addAttribute("paymentID", payId);
		model.addAttribute("currentUser", currentUser);

		return "transfer_page";
	}

	@GetMapping("/register")
	public String registeringNewPerson(Model model, HttpSession session) {

		model.addAttribute("newUser", new LoginRegistration());

		return "register";
	}

	@PostMapping("/process_register")
	public String processRegistration(LoginRegistration person, HttpSession session, Model model) {

		if (userServices.checkExistingMail(person)) {

			model.addAttribute("existingmail", true);
			model.addAttribute("newUser", new LoginRegistration());

			return "register";

		}

		if (!person.getPassword().equals(person.getSecondTestPassword())) {

			model.addAttribute("passwordmatch", true);
			model.addAttribute("newUser", new LoginRegistration());

			return "register";

		}

		userServices.saveNewPerson(person);

		return "register_successfully";

	}

	@GetMapping("/Logoff")
	public String logOfftheAccount() {

		return "log_off";
	}

	private void RefreshAndInitializeAllImportantData(HttpSession session) {

		currentUser = (Person) session.getAttribute("currentUser");

		listOfAllTransactions = currentUser.getAllTransactions();
		listOfBuddies = userServices.getListNoDuplicates(listOfAllTransactions, currentUser);

	}

}

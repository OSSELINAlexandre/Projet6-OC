package com.example.paymybuddy.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.paymybuddy.DTO.BankAccountWithdrawalDepositInformation;
import com.example.paymybuddy.DTO.BuddiesInConnexion;
import com.example.paymybuddy.DTO.IdentificationData;
import com.example.paymybuddy.DTO.LoginRegistration;
import com.example.paymybuddy.DTO.PaymentData;
import com.example.paymybuddy.model.BankOperation;
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
	private List<BankOperation> listOfAllOperation;
	private Map<Person, String> listOfBuddies;

	@GetMapping("/")
	public String returnMainPage(@RequestParam("errorFlag") Optional<Boolean> errorFlag, Model model) {

		model.addAttribute("comingUser", new IdentificationData());

		if (errorFlag.isPresent()) {

			model.addAttribute("errorFlag", true);
		}

		return "login";
	}

	@GetMapping("/profileUser")
	public String returnUserProfil(HttpSession session) {
		refreshAndInitializeAllImportantData(session);

		return "user_profile";
	}

	@GetMapping("/Logoff")
	public String logOfftheAccount() {

		return "log_off";
	}

	@GetMapping("/register")
	public String registeringNewPerson(Model model, HttpSession session) {

		model.addAttribute("newUser", new LoginRegistration());

		return "register";
	}

	@GetMapping("/registersucessfully")
	public String registerIsASucess() {

		return "register_successfully";
	}

	@PostMapping("/process_signin")
	public ModelAndView verifyIdentity(IdentificationData person, Model model, HttpSession session) {

		currentUser = userServices.findByIdentificationDataLogin(person);

		if (currentUser == null) {

			ModelAndView theview = new ModelAndView("redirect:http://localhost:8080/");
			theview.addObject("errorFlag", true);
			return theview;

		} else {

			ModelAndView theview = new ModelAndView("redirect:http://localhost:8080/userHome");

			listOfAllTransactions = currentUser.getAllTransactions();
			listOfAllOperation = userServices.getAllOperations(currentUser);
			listOfBuddies = userServices.getListOfBuddies(currentUser);

			session.setAttribute("listOfBuddies", listOfBuddies);
			session.setAttribute("currentUser", currentUser);
			session.setAttribute("listTransactions", listOfAllTransactions);
			session.setAttribute("listOfAllOperations", listOfAllOperation);

			return theview;

		}

	}

	@PostMapping("/process_register")
	public ModelAndView processRegistration(LoginRegistration person, HttpSession session, Model model) {

		if (userServices.checkExistingMail(person)) {

			ModelAndView theView = new ModelAndView("redirect:http://localhost:8080/register");
			theView.addObject("existingmail", true);
			return theView;

		} else if (!person.getPassword().equals(person.getSecondTestPassword())) {

			ModelAndView theView = new ModelAndView("redirect:http://localhost:8080/register");
			theView.addObject("passwordmatch", true);
			return theView;

		} else {

			ModelAndView theView = new ModelAndView("redirect:http://localhost:8080/registersucessfully");
			userServices.saveNewPerson(person);

			return theView;

		}

	}

	private void refreshAndInitializeAllImportantData(HttpSession session) {

		currentUser = (Person) session.getAttribute("currentUser");
		listOfAllTransactions = currentUser.getAllTransactions();
		listOfAllOperation = userServices.getAllOperations(currentUser);

	}

}

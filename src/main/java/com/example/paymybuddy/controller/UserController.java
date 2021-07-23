package com.example.paymybuddy.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.paymybuddy.DTO.LoginRegistration;
import com.example.paymybuddy.model.BankOperation;
import com.example.paymybuddy.model.ConnexionBetweenBuddies;
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
	private List<BankOperation> listOfAllOperations;
	private List<ConnexionBetweenBuddies> listOfAllConnexionOfBuddies;

	@GetMapping("/setuserattributes")
	public ModelAndView setAllTheDataFromAuthenticatedUser(HttpSession session) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		logger.info("================================================||||||||||||||||||||||"
				+ authentication.getAuthorities().toString());
		currentUser = userServices.getThePersonAfterAuthentication(authentication.getName());

		ModelAndView theview = new ModelAndView("redirect:/userHome");

		listOfAllTransactions = currentUser.getAllTransactions();
		listOfAllOperations = currentUser.getListOfALLOperations();
		listOfAllConnexionOfBuddies = currentUser.getListOfBuddies();

		session.setAttribute("currentUser", currentUser);
		session.setAttribute("listOfAllConnexionOfBuddies", listOfAllConnexionOfBuddies);
		session.setAttribute("listOfAllTransactions", listOfAllTransactions);
		session.setAttribute("listOfAllOperations", listOfAllOperations);

		return theview;

	}

	@GetMapping("/login")
	public String returnMainPage(@RequestParam("errorFlag") Optional<Boolean> errorFlag, Model model) {

		return "login";
	}

	@GetMapping("/profileUser")
	public String returnUserProfile(HttpSession session) {

		return "user_profile";
	}

	@GetMapping("/register")
	public String registeringNewPerson(@RequestParam("passwordmatch") Optional<Boolean> passwordmatch,
			@RequestParam("existingmail") Optional<Boolean> existingmail, Model model, HttpSession session) {

		model.addAttribute("newUser", new LoginRegistration());

		if (passwordmatch.isPresent()) {
			model.addAttribute("passwordmatch", true);
		}
		if (existingmail.isPresent()) {
			model.addAttribute("existingmail", true);
		}

		return "register";
	}

	@GetMapping("/registersucessfully")
	public String registerIsASucess() {

		return "register_successfully";
	}

	@PostMapping("/process_register")
	public ModelAndView processRegistration(LoginRegistration person, HttpSession session, Model model) {

		if (userServices.checkIfTheEmailExistsInTheDB(person)) {

			ModelAndView theView = new ModelAndView("redirect:/register");
			theView.addObject("existingmail", true);
			return theView;

		} else if (!person.getPassword().equals(person.getSecondTestPassword())) {

			ModelAndView theView = new ModelAndView("redirect:/register");
			theView.addObject("passwordmatch", true);
			return theView;

		} else {

			ModelAndView theView = new ModelAndView("redirect:/registersucessfully");
			userServices.saveANewPersonInTheDB(person);

			return theView;

		}

	}

}

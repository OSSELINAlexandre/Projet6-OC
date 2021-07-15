package com.example.paymybuddy.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.paymybuddy.model.Person;

@Controller
public class ContactController {
	
	private Person currentUser;
	
	/**
	 * This controller is a mock to show how the architecture would be done if the contact form was a requirement from the client;
	 * Each business domain would have all it's mapping in a single controller named after it.
	 * @param session
	 * @return
	 */
	
	
	@GetMapping("/contact")
	public String returnContactPages(HttpSession session) {
		refreshAndInitializeAllImportantData(session);

		return "contact";
	}
	
	
	private void refreshAndInitializeAllImportantData(HttpSession session) {

		currentUser = (Person) session.getAttribute("currentUser");

	}

}

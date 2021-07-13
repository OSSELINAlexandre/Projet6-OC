package com.example.paymybuddy.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.paymybuddy.model.Person;

@Controller
public class ContactController {
	
	private Person currentUser;
	
	@GetMapping("/contact")
	public String returnContactPages(HttpSession session) {
		refreshAndInitializeAllImportantData(session);

		return "contact";
	}
	
	
	private void refreshAndInitializeAllImportantData(HttpSession session) {

		currentUser = (Person) session.getAttribute("currentUser");

	}

}

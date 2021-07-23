package com.example.paymybuddy.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactController {

	/**
	 * This controller is a mock to show how the architecture would be done if the
	 * contact form was a requirement from the client; Each business domain would
	 * have all it's mapping in a single controller named after it.
	 * 
	 * @param session
	 * @return
	 */

	@GetMapping("/contact")
	public String returnContactPage(HttpSession session) {

		return "contact";
	}

}

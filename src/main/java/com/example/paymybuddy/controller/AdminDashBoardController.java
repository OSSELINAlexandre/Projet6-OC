package com.example.paymybuddy.controller;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.paymybuddy.DTO.AdminDataForDashboard;
import com.example.paymybuddy.service.AdminServices;

@Controller
public class AdminDashBoardController {

	@Autowired
	AdminServices adminServices;

	@GetMapping("/dashboard")
	public String getTheDashBoardForAdmin(Model model) {

		List<AdminDataForDashboard> resultItem = adminServices.generateDashBoard();
		model.addAttribute("administrativeList", resultItem);

		return "dashboard";
	}

}

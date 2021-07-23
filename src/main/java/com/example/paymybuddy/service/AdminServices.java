package com.example.paymybuddy.service;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.paymybuddy.DTO.AdminDataForDashboard;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.model.Transaction;

import com.example.paymybuddy.repository.PersonRepository;
import com.example.paymybuddy.repository.TransactionRepository;

@Service
public class AdminServices {

	@Autowired
	PersonRepository personRepository;

	@Autowired
	TransactionRepository transactionRepository;

	public List<AdminDataForDashboard> generateDashBoard() {

		List<AdminDataForDashboard> adminDashList = new ArrayList<>();

		for (Person p : personRepository.findAll()) {

			List<Transaction> result = transactionRepository.findByPayer(p);
			int numberOfTransac = result.size();
			
			if(numberOfTransac > 0 ) {
			AdminDataForDashboard newItem = new AdminDataForDashboard();
			newItem.setFeePayed(p.getTotalamountpayedfee());
			newItem.setLastName(p.getLastName());
			newItem.setName(p.getName());
			newItem.setNumberOfTransac(numberOfTransac);
			adminDashList.add(newItem);
			}

		}

		return adminDashList;
	}
	
	// ====== Setters of repository solely needed for testing purposes.
	// ====== Once the app is validated, and for security reasons, these setters
	// ====== can be deleted



	public void setPersonRepo(PersonRepository personRepo) {
		this.personRepository = personRepo;
	}



	public void setTransactionRepo(TransactionRepository transactionRepo) {
		this.transactionRepository = transactionRepo;
	}
	

	

}

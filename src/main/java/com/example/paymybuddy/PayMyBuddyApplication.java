package com.example.paymybuddy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.model.Transaction;
import com.example.paymybuddy.service.UserServices;
import com.example.paymybuddy.service.TransactionsServices;

@SpringBootApplication
public class PayMyBuddyApplication {


	public static void main(String[] args) {
		SpringApplication.run(PayMyBuddyApplication.class, args);
	}



}

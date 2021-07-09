package com.example.premierbrouillonapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.premierbrouillonapplication.model.Person;
import com.example.premierbrouillonapplication.model.Transaction;
import com.example.premierbrouillonapplication.service.PersonServices;
import com.example.premierbrouillonapplication.service.TransactionsServices;

@SpringBootApplication
public class PremierbrouillonapplicationApplication {


	public static void main(String[] args) {
		SpringApplication.run(PremierbrouillonapplicationApplication.class, args);
	}



}

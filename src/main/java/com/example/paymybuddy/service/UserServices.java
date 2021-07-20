package com.example.paymybuddy.service;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.paymybuddy.DTO.IdentificationData;
import com.example.paymybuddy.DTO.LoginRegistration;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.repository.PersonRepository;

@Service
public class UserServices implements UserDetailsService {

	
	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(UserServices.class);

	
	
	@Autowired
	PersonRepository personRepo;

	public UserServices() {
	}

	public void saveANewPersonInTheDB(LoginRegistration person) {

		Person newItem = new Person();
		newItem.seteMail(person.geteMail());
		newItem.setLastName(person.getLastName());
		newItem.setName(person.getName());
		newItem.setPassword(passwordEncoderSecond().encode(person.getPassword()));
		newItem.setAmount(0.0);

		personRepo.save(newItem);

	}

	public boolean checkIfTheEmailExistsInTheDB(LoginRegistration person) {

		if (personRepo.findByEmail(person.geteMail()) != null) {

			return true;

		} else {

			return false;
		}

	}

	public Person findByIdentificationDataIfCombinasioExists(IdentificationData person) {

		return personRepo.findByEmailAndPassword(person.getEmail(), person.getPassword());

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		logger.info("=================" + username);
		return personRepo.findByEmail(username);
	}
	
	
	public Person getThePersonAfterAuthentication(String email) {
		
		return personRepo.findByEmail(email);
	}
	

	@Bean
	public BCryptPasswordEncoder passwordEncoderSecond() {

		return new BCryptPasswordEncoder();
	}

}

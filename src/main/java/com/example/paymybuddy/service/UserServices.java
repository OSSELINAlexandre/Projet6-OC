package com.example.paymybuddy.service;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
		newItem.setEmail(person.geteMail());
		newItem.setLastName(person.getLastName());
		newItem.setName(person.getName());
		newItem.setPassword(passwordEncoderSecond().encode(person.getPassword()));
		newItem.setAccountFunds(0.0);
		newItem.setTotalamountpayedfee(0.00);
		newItem.setAuthority("USER");

		personRepo.save(newItem);

	}

	public boolean checkIfTheEmailExistsInTheDB(LoginRegistration person) {

		if (personRepo.findByEmail(person.geteMail()) != null) {

			return true;

		} else {

			return false;
		}

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Person triesToAuthenticate = personRepo.findByEmail(username);

		if (triesToAuthenticate == null) {

			throw new UsernameNotFoundException(username);

		}

		UserDetails user = User.withUsername(triesToAuthenticate.getEmail()).password(triesToAuthenticate.getPassword())
				.authorities(triesToAuthenticate.getAuthority()).build();

		logger.info("=============" + triesToAuthenticate.getAuthority());
		return user;
	}

	public Person getThePersonAfterAuthentication(String email) {

		return personRepo.findByEmail(email);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoderSecond() {

		return new BCryptPasswordEncoder();
	}

	// ====== Getters and Setters of repository solely needed for testing purposes.
	// ====== Once the app is validated, and for security reasons, these getters and
	// setters
	// ====== can be deleted

	public PersonRepository getPersonRepo() {
		return personRepo;
	}

	public void setPersonRepo(PersonRepository personRepo) {
		this.personRepo = personRepo;
	}

}

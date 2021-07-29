package com.example.paymybuddy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "Person")
@Transactional
public class Person implements Serializable, UserDetails {

	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(Person.class);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_person")
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "surname")
	private String lastName;

	@Column(name = "email")
	private String email;
	
	@Column(name = "password")
	private String password;
	

	@Column(name = "authorization")
	private String authority;
	

	@Column(name = "accountfunds")
	private Double accountFunds;


	@Column(name = "totalamountpayedfee")
	private Double totalamountpayedfee;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "willpayperson", nullable = true)
	private List<Transaction> transactionsPayed;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "willbepayedperson", nullable = true)
	private List<Transaction> transactionsThatWerePayed;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "accountholder", nullable = true)
	private List<BankOperation> listOfALLOperations;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "user", nullable = true)
	private List<ConnectionBetweenBuddies> listOfBuddies;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "accountowner", nullable = true)
	private List<ExternalBankAccount> listOfAllBankAccountOwned;

	public Person() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Transaction> getTransactionsPayed() {
		return transactionsPayed;
	}

	public void setTransactionsPayed(List<Transaction> transactionsPayed) {
		this.transactionsPayed = transactionsPayed;
	}

	public List<Transaction> getTransactionsThatWerePayed() {
		return transactionsThatWerePayed;
	}

	public void setTransactionsThatWerePayed(List<Transaction> transactionsThatWerePayed) {
		this.transactionsThatWerePayed = transactionsThatWerePayed;
	}

	public Double getTotalamountpayedfee() {
		return totalamountpayedfee;
	}

	public void setTotalamountpayedfee(Double d) {
		this.totalamountpayedfee = d;
	}

	public Double getAccountFunds() {
		return accountFunds;
	}

	public void setAccountFunds(Double accountFunds) {
		this.accountFunds = accountFunds;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public List<Transaction> getAllTransactions() {

		List<Transaction> result = new ArrayList<Transaction>();
		result.addAll(transactionsThatWerePayed);
		result.addAll(transactionsPayed);
		return result;

	}

	public List<Transaction> addTransitoryTransaction(Transaction transitoryItem) {

		transactionsPayed.add(transitoryItem);
		return getAllTransactions();

	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<BankOperation> getListOfALLOperations() {
		return listOfALLOperations;
	}

	public void setListOfALLOperations(List<BankOperation> listOfALLOperations) {
		this.listOfALLOperations = listOfALLOperations;
	}

	@Transactional
	public List<ConnectionBetweenBuddies> getListOfBuddies() {

		logger.info(
				"Lazy Fetch need to be initialize in the transactional context in order to be avaiable in the session as an attribute (the real list can be given and not just the proxy)"
						+ listOfBuddies.size());

		return listOfBuddies;
	}

	public void setListOfBuddies(List<ConnectionBetweenBuddies> listOfBuddies) {
		this.listOfBuddies = listOfBuddies;
	}

	
	
	public List<ExternalBankAccount> getListOfAllBankAccountOwned() {
		
		logger.info(
				"Test List size"
						+ listOfAllBankAccountOwned.size());
		
		return listOfAllBankAccountOwned;
	}

	public void setListOfAllBankAccountOwned(List<ExternalBankAccount> listOfAllBankAccountOwned) {
		this.listOfAllBankAccountOwned = listOfAllBankAccountOwned;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}

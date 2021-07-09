package com.example.paymybuddy.model;

import java.util.ArrayList;
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

@Entity
@Table(name = "Person")
public class Person {

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

	@Column(name = "accountfunds")
	private Double accountfunds;

	@Column(name = "password")
	private String password;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "willpayperson", nullable = true)
	private List<Transaction> transactionsPayed;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "willbepayedperson", nullable = true)
	private List<Transaction> transactionsThatWasPayed;

	public Person(int id, String name, String lastName, String eMail, String password, Double amount) {
		super();
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.email = eMail;
		this.password = password;
		this.accountfunds = amount;
	}

	public Person(int id, String name, String lastName, String eMail, String password) {
		super();
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.email = eMail;
		this.password = password;
		this.accountfunds = 0.0;
	}

	public Person() {
		super();
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

	public String geteMail() {
		return email;
	}

	public void seteMail(String eMail) {
		this.email = eMail;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getAmount() {
		return accountfunds;
	}

	public void setAmount(Double amount) {
		this.accountfunds = amount;
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

	public List<Transaction> getTransactionsThatWasPayed() {
		return transactionsThatWasPayed;
	}

	public void setTransactionsThatWasPayed(List<Transaction> transactionsThatWasPayed) {
		this.transactionsThatWasPayed = transactionsThatWasPayed;
	}

	public List<Transaction> getAllTransactions() {

		List<Transaction> result = new ArrayList<Transaction>();
		result.addAll(transactionsThatWasPayed);
		result.addAll(transactionsPayed);
		return result;

	}

}

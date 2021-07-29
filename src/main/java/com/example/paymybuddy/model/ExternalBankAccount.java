package com.example.paymybuddy.model;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "externalbankaccount")
public class ExternalBankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_bankaccount")
	private int id;
	
	@Column(name = "bankname ")
	private String bankname;
	

	@Column(name = "location")
	private String location;

	@Column(name = "iban")
	private String iban;

	@Column(name = "biccode")
	private String biccode;

	@Column(name = "currency")
	private String currency;

	@OneToOne
	@JoinColumn(name = "accountowner", referencedColumnName = "id_person")
	private Person accountOwner;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.LAZY)
	@JoinColumn(name = "originbankaccount", nullable = true)
	private List<BankOperation> listOfOperationDoneOnThisAccount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBiccode() {
		return biccode;
	}

	public void setBiccode(String biccode) {
		this.biccode = biccode;
	}

	public Person getAccountOwner() {
		return accountOwner;
	}

	public void setAccountOwner(Person accountOwner) {
		this.accountOwner = accountOwner;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<BankOperation> getListOfOperationDoneOnThisAccount() {
		return listOfOperationDoneOnThisAccount;
	}

	public void setListOfOperationDoneOnThisAccount(List<BankOperation> listOfOperationDoneOnThisAccount) {
		this.listOfOperationDoneOnThisAccount = listOfOperationDoneOnThisAccount;
	}


	
	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	@Override
	public String toString() {
		return "ExternalBankAccount [id=" + id + ", location=" + location + ", iban=" + iban + ", biccode=" + biccode
				+ ", currency=" + currency + ", accountOwner=" + accountOwner + ", listOfOperationDoneOnThisAccount="
				+ listOfOperationDoneOnThisAccount + "]";
	}

	
	
	
}

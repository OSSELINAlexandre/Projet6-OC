package com.example.premierbrouillonapplication.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Bank_Account")
public class BankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_account")
	private int id;

	@Column(name = "Amount")
	private Double amount;


	@OneToOne
	@JoinColumn(name = "Accountholder", referencedColumnName = "id_person")
	private Person holder;

	public BankAccount() {
		super();
	}

	public BankAccount(int id, Double amount) {
		super();
		this.id = id;
		this.amount = amount;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public Person getHolder() {
		return holder;
	}

	public void setHolder(Person holder) {
		this.holder = holder;
	}

}

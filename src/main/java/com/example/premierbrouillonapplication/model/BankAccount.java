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
@Table(name = "bank_account")
public class BankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_Account")
	private int id;

	@Column(name = "Amount")
	private Double amount;

	@Column(name = "Level_subscription")
	private String levelSub;

	@OneToOne
	@JoinColumn(name = "ID_Person", referencedColumnName = "ID_Person")
	private Person holder;

	public BankAccount() {
		super();
	}

	public BankAccount(int id, Double amount, String levelSub) {
		super();
		this.id = id;
		this.amount = amount;
		this.levelSub = levelSub;
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

	public String getLevelSub() {
		return levelSub;
	}

	public void setLevelSub(String levelSub) {
		this.levelSub = levelSub;
	}

	public Person getHolder() {
		return holder;
	}

	public void setHolder(Person holder) {
		this.holder = holder;
	}

}

package com.example.premierbrouillonapplication.model;

import java.util.List;

import javax.persistence.Entity;

public class PaymentData {

//	private Person personPayingTheAmount;
	private String personToPay;
	private Integer amount;
	private String description;

	
	public PaymentData() {
		super();
	}
	
	public PaymentData(String personToPay, Integer amount, String description) {
		super();
		this.personToPay = personToPay;
		this.amount = amount;
		this.description = description;
	}
	public String getPersonToPay() {
		return personToPay;
	}
	public void setPersonToPay(String personToPay) {
		this.personToPay = personToPay;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}



}

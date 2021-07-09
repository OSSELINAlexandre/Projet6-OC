package com.example.paymybuddy.DTO;

import java.util.List;

import javax.persistence.Entity;

public class PaymentData {

//	private Person personPayingTheAmount;
	private String personToPay;
	private Double amount;
	private String description;

	
	public PaymentData() {
		super();
	}
	
	public PaymentData(String personToPay, Double amount, String description) {
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
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}



}

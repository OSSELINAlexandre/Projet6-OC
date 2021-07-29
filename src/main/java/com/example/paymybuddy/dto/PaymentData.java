package com.example.paymybuddy.dto;

public class PaymentData {

	private String personToPay;
	private Double amount;
	private String description;

	public PaymentData() {
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

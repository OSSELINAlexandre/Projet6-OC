package com.example.premierbrouillonapplication.model;

public class BankAccountWithdrawalDepositInformation {

	private Double amount;

	public BankAccountWithdrawalDepositInformation(Double amount) {
		super();
		this.amount = amount;
	}

	public BankAccountWithdrawalDepositInformation() {
		super();
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}

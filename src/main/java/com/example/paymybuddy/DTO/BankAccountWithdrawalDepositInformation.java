package com.example.paymybuddy.DTO;

public class BankAccountWithdrawalDepositInformation {

	private Double amount;

	public BankAccountWithdrawalDepositInformation(Double amount) {
		this.amount = amount;
	}

	public BankAccountWithdrawalDepositInformation() {
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}

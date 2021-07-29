package com.example.paymybuddy.dto;

import com.example.paymybuddy.model.ExternalBankAccount;

public class BankAccountWithdrawalDepositInformation {

	private Double amount;

	private String bankAccountToDoOperationID;

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

	public String getBankAccountToDoOperationID() {
		return bankAccountToDoOperationID;
	}

	public void setBankAccountToDoOperationID(String bankAccountToDoOperationID) {
		this.bankAccountToDoOperationID = bankAccountToDoOperationID;
	}
	
	

}

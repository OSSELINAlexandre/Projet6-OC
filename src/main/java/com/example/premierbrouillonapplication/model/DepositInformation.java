package com.example.premierbrouillonapplication.model;

public class DepositInformation {

	private String AccountNumber;

	private Integer AmountToDeposit;
	

	public DepositInformation() {
		super();
	}

	public DepositInformation(String accountNumber, Integer amountToDeposit) {
		super();
		AccountNumber = accountNumber;
		AmountToDeposit = amountToDeposit;
	}

	public String getAccountNumber() {
		return AccountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		AccountNumber = accountNumber;
	}

	public Integer getAmountToDeposit() {
		return AmountToDeposit;
	}

	public void setAmountToDeposit(Integer amountToDeposit) {
		AmountToDeposit = amountToDeposit;
	}

}

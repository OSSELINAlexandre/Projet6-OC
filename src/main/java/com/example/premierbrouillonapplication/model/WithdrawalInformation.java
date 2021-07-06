package com.example.premierbrouillonapplication.model;

public class WithdrawalInformation {

	private Integer amount;

	public WithdrawalInformation(Integer amount) {
		super();
		this.amount = amount;
	}

	public WithdrawalInformation() {
		super();
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

}

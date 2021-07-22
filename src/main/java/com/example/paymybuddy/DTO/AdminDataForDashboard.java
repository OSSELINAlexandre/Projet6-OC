package com.example.paymybuddy.DTO;

public class AdminDataForDashboard {
	
	private String name;
	
	private String lastName;
	
	private Integer numberOfTransac;
	
	private Double feePayed;

	public AdminDataForDashboard() {
		super();
	}

	public AdminDataForDashboard(String name, String lastName, Integer numberOfTransac, Double feePayed) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.numberOfTransac = numberOfTransac;
		this.feePayed = feePayed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getNumberOfTransac() {
		return numberOfTransac;
	}

	public void setNumberOfTransac(Integer numberOfTransac) {
		this.numberOfTransac = numberOfTransac;
	}

	public Double getFeePayed() {
		return feePayed;
	}

	public void setFeePayed(Double feePayed) {
		this.feePayed = feePayed;
	}
	
	

}

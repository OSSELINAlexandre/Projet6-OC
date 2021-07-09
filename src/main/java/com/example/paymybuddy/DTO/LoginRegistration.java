package com.example.paymybuddy.DTO;

public class LoginRegistration {

	private String name;

	private String lastName;

	private String email;

	private String password;

	private String secondTestPassword;

	public LoginRegistration() {
		super();
	}

	public LoginRegistration(String name, String lastName, String eMail, String password, String secondTestPassword) {
		this.name = name;
		this.lastName = lastName;
		this.email = eMail;
		this.password = password;
		this.secondTestPassword = secondTestPassword;
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

	public String geteMail() {
		return email;
	}

	public void seteMail(String eMail) {
		this.email = eMail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSecondTestPassword() {
		return secondTestPassword;
	}

	public void setSecondTestPassword(String secondTestPassword) {
		this.secondTestPassword = secondTestPassword;
	}

}

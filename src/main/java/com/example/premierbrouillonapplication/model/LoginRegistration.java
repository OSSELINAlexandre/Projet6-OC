package com.example.premierbrouillonapplication.model;

public class LoginRegistration {

	private String name;

	private String lastName;

	private String eMail;

	private String password;

	public LoginRegistration() {
		super();
	}

	public LoginRegistration(String name, String lastName, String eMail, String password) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.eMail = eMail;
		this.password = password;
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
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

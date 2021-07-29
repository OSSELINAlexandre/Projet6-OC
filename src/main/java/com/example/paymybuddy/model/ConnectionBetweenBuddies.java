package com.example.paymybuddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Buddiesconnection")
public class ConnectionBetweenBuddies {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_connexion")
	private int id;

	@Column(name = "user")
	private int idOfCenter;

	@Column(name = "buddyofuser")
	private int buddyOfACenter;

	public ConnectionBetweenBuddies() {
		super();
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdOfCenter() {
		return idOfCenter;
	}

	public void setIdOfCenter(int idOfCenter) {
		this.idOfCenter = idOfCenter;
	}

	public int getBuddyOfACenter() {
		return buddyOfACenter;
	}

	public void setBuddyOfACenter(int buddyOfACenter) {
		this.buddyOfACenter = buddyOfACenter;
	}

}

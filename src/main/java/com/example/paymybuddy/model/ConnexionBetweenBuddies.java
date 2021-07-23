package com.example.paymybuddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "buddiesconnexion")
public class ConnexionBetweenBuddies {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_connexion")
	private int id;

	@Column(name = "center")
	private int idOfCenter;

	@Column(name = "buddyofcenter")
	private int buddyOfACenter;

	public ConnexionBetweenBuddies() {
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

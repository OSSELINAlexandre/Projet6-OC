package com.example.premierbrouillonapplication.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_Transactions")
	private int id;

	@Column(name = "Commentaire")
	private String commentaire;

	@Column(name = "Amount")
	private Integer amount;

	@OneToOne
	@JoinColumn(name = "willPayPerson", referencedColumnName = "ID_Person")
	private Person payeur;

	@OneToOne
	@JoinColumn(name = "wilBePayPerson", referencedColumnName = "ID_Person")
	private Person payee;

	public Transaction() {
		super();
	}
	
	public Transaction(String commentaire, Integer amount, Person payeur, Person payee) {
		super();
		this.commentaire = commentaire;
		this.amount = amount;
		this.payeur = payeur;
		this.payee = payee;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Person getPayeur() {
		return payeur;
	}

	public void setPayeur(Person payeur) {
		this.payeur = payeur;
	}

	public Person getPayee() {
		return payee;
	}

	public void setPayee(Person payee) {
		this.payee = payee;
	}

}

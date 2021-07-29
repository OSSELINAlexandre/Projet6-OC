package com.example.paymybuddy.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Transaction")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_transactions")
	private int id;

	@Column(name = "commentary")
	private String commentaire;

	@Column(name = "amount")
	private Double amount;


	@Column(name = "feeontransaction")
	private Double feeOnTransaction;

	@OneToOne
	@JoinColumn(name = "willpayperson", referencedColumnName = "id_person")
	private Person payer;

	@OneToOne
	@JoinColumn(name = "willbepayedperson", referencedColumnName = "id_person")
	private Person payee;


	public Transaction() {
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double integer) {
		this.amount = integer;
	}

	public Person getPayer() {
		return payer;
	}

	public void setPayer(Person payeur) {
		this.payer = payeur;
	}

	public Person getPayee() {
		return payee;
	}

	public void setPayee(Person payee) {
		this.payee = payee;
	}

	public Double getFeeOnTransaction() {
		return feeOnTransaction;
	}

	public void setFeeOnTransaction(Double feeontransaction) {
		this.feeOnTransaction = feeontransaction;
	}

}

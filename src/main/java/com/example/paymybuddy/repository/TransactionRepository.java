package com.example.paymybuddy.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.model.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

	List<Transaction> findByPayeur(Person id);

}

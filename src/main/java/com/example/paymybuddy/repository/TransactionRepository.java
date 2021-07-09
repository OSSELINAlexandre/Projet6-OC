package com.example.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.paymybuddy.model.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

}

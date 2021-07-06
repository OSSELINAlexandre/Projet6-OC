package com.example.premierbrouillonapplication.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.premierbrouillonapplication.model.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

}

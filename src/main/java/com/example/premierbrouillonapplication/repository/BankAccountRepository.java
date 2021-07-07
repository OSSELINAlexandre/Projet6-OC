package com.example.premierbrouillonapplication.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.premierbrouillonapplication.model.BankAccount;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Integer> {

	public Iterable<BankAccount> findByholder(int id);
}

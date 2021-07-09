package com.example.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.paymybuddy.model.BankOperation;

@Repository
public interface BankOperationRepository extends CrudRepository<BankOperation, Integer> {

	public Iterable<BankOperation> findByholder(int id);
}

package com.example.paymybuddy.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.paymybuddy.model.BankOperation;
import com.example.paymybuddy.model.Person;

@Repository
public interface BankOperationRepository extends CrudRepository<BankOperation, Integer> {

	public Iterable<BankOperation> findByholder(Person id);

}

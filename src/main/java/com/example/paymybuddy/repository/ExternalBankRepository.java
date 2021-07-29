package com.example.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.paymybuddy.model.ExternalBankAccount;

public interface ExternalBankRepository extends CrudRepository<ExternalBankAccount, Integer>{

	
	ExternalBankAccount findByIbanAndBiccodeAndBankname(String iban, String bic, String bankName);

	
}

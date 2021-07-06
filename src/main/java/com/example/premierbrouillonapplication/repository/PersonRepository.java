package com.example.premierbrouillonapplication.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.premierbrouillonapplication.model.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {

}

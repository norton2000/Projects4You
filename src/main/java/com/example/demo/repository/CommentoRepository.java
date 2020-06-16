package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Commento;

@Repository
public interface CommentoRepository extends CrudRepository<Commento, Long> {

}

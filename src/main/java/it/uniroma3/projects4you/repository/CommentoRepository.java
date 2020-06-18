package it.uniroma3.projects4you.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.projects4you.model.Commento;

@Repository
public interface CommentoRepository extends CrudRepository<Commento, Long> {

}

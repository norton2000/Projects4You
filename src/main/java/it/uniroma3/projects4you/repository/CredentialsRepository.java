package it.uniroma3.projects4you.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.projects4you.model.Credentials;

@Repository
public interface CredentialsRepository extends CrudRepository<Credentials, Long>{
	
	public Optional<Credentials> findByUsername(String username);
	
	public void deleteByUsername(String username);
}

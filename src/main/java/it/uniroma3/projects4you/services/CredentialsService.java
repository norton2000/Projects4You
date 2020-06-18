package it.uniroma3.projects4you.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.projects4you.model.Credentials;
import it.uniroma3.projects4you.repository.CredentialsRepository;

@Service
public class CredentialsService {
	
	@Autowired
	protected PasswordEncoder passwordEncorder;
	
	@Autowired
	protected CredentialsRepository credentialsRepository;
	
	@Transactional
	public Credentials getCredentials(long id) {
		Optional<Credentials> result = this.credentialsRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public Credentials getCredentials(String username) {
		Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
		return result.orElse(null);
	}
	
	@Transactional
	public Credentials saveCredentials(Credentials credentials) {
		credentials.setRole(Credentials.DEFAULT_ROLE);
		credentials.setPassword(this.passwordEncorder.encode(credentials.getPassword()));
		return this.credentialsRepository.save(credentials);
	}

	@Transactional
	public List<Credentials> getAllCredentials() {
		Iterable<Credentials> i = credentialsRepository.findAll();
		ArrayList<Credentials> lista = new ArrayList<>();
		for(Credentials c : i) {
			lista.add(c);
		}
		return lista;
	}

	@Transactional
	public void deleteCredentials(String username) {
		Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
		if(result.isPresent())
			this.credentialsRepository.deleteById(result.get().getId());
	}
}

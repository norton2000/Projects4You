package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Project;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	
	
	@Autowired
	private UserRepository userRepository;
	

	
	@Transactional
	public User getUser(Long id) {
		Optional<User> result = this.userRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public User getUser(String nickname) {
		User result = this.userRepository.findByNickname(nickname);
		return result;
	}
	
	@Transactional
	public User saveUser(User user) {
		user.setLastUpdateTimestamp();
		return this.userRepository.save(user);
	}
	
	@Transactional
	public List<User> findAllUsers(User user) {
		Iterable<User> i = this.userRepository.findAll();
		ArrayList<User> lista = new ArrayList<>();
		for(User u : i) {
			lista.add(u);
		}
		return lista;
	}

	@Transactional
	public List<User> getMembers(Project project) {
		return userRepository.findByVisibleProjects(project);
	}


	
	
	
}

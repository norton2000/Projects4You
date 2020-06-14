package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Project;
import com.example.demo.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
	public List<User> findByVisibleProjects(Project project);
	
	
	@Modifying(clearAutomatically = true)
	@Query("UPDATE User u SET u.firstname = ?2, u.lastname = ?3 WHERE u.id = ?1")
	public void updateUserProfile(Long id, String firstname, String lastname);
}

package com.example.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Project;
import com.example.demo.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
	public List<User> findByVisibleProjects(Project project);
	
	public User findByNickname(String nickname);

	/*
	@Query(value = "SELECT u FROM users u "
			+ "WHERE u.id != ?2 AND NOT EXISTS (SELECT mem FROM project_members mem "
														+ "WHERE mem.visible_projects_id = ?1 AND mem.members_id = u.id)", 
														nativeQuery = true)
	public List<User> getUsersWhoCanShare(Long project_id, Long owner_id);
	
	*/
	
}

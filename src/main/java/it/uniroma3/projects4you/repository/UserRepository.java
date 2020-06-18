package it.uniroma3.projects4you.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.projects4you.model.Project;
import it.uniroma3.projects4you.model.User;

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

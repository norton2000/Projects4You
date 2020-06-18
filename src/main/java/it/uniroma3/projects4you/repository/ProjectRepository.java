package it.uniroma3.projects4you.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.projects4you.model.Project;
import it.uniroma3.projects4you.model.User;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
	
	
	public List<Project> findByMembers(User member);
	
	public List<Project> findByOwner(User owner);
}

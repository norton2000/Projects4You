package it.uniroma3.projects4you.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.projects4you.model.Task;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long>{
	
}

package it.uniroma3.projects4you.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.projects4you.model.Commento;
import it.uniroma3.projects4you.model.Project;
import it.uniroma3.projects4you.model.Tag;
import it.uniroma3.projects4you.model.Task;
import it.uniroma3.projects4you.model.User;
import it.uniroma3.projects4you.repository.CommentoRepository;
import it.uniroma3.projects4you.repository.TaskRepository;

@Service
public class TaskService {
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private CommentoRepository commentoRepository;
	
	@Transactional
	public Task getTask(Long id) {
		Optional<Task> result = this.taskRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public void deleteTask(Project project, Task task) {
		project.deleteTask(task);
		this.taskRepository.deleteById(task.getId());
	}
	
	@Transactional
	public Task saveTask(Task task) {
		return this.taskRepository.save(task);
	}
	
	@Transactional
	public Task setCompletedTask(Task task) {
		task.setCompleted(true);
		return this.taskRepository.save(task);
	}
	
	@Transactional
	public void assegnaTask(Task task, User user) {
		task.setUser(user);
		this.saveTask(task);
	}
	
	@Transactional
	public void creaNuovoTask(Project project, Task task) {
		task.setCreationTimestamp(LocalDateTime.now());
		task.setProject(project);
		this.taskRepository.save(task);
	}

	@Transactional
	public void addComment(Task task, Commento commento, User user) {
		
		commento.setLeavedBy(user);
		task.addComment(commento);
		this.commentoRepository.save(commento);
	}
	
	@Transactional
	public Task removeTag(Tag tag, Task task) {
		tag.removeTask(task);
		task.removeTag(tag);
		return this.taskRepository.save(task);
	}

	@Transactional
	public Task addTag(Tag tag, Task task) {
		tag.addTask(task);
		task.addTag(tag);
		return this.taskRepository.save(task);
	}
	
}

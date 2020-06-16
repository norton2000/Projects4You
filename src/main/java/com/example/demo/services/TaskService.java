package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Commento;
import com.example.demo.model.Project;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.CommentoRepository;
import com.example.demo.repository.TaskRepository;

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
	public void deleteTask(Task task) {
		this.taskRepository.deleteById(task.getId());
	}
	
	@Transactional
	public Task saveTask(Task task) {
		task.updateLastUpdateTimestamp();
		return this.taskRepository.save(task);
	}
	
	@Transactional
	public Task setCompletedTask(Task task) {
		task.setCompleted(true);
		return this.taskRepository.save(task);
	}
	
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
}

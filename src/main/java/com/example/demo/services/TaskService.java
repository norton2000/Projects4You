package com.example.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;

@Service
public class TaskService {
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Transactional
	public Task getTask(Long id) {
		Optional<Task> result = this.taskRepository.findById(id);
		return result.orElse(null);
	}
	
	public void deleteTask(Task task) {
		this.taskRepository.delete(task);
	}
	
	public Task saveTask(Task task) {
		task.updateLastUpdateTimestamp();
		return this.taskRepository.save(task);
	}
	
	public Task setCompletedTask(Task task) {
		task.setCompleted(true);
		return this.taskRepository.save(task);
	}
}

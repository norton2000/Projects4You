package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Project;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskRepository;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Transactional
	public Project getProject(Long id) {
		Optional<Project> result = this.projectRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public List<Project> retrieveProjectsOwnedBy(User user) {
		return this.projectRepository.findByOwner(user);
		}
	
	@Transactional
	public Project saveProject(Project project) {
		return this.projectRepository.save(project);
	}
	
	@Transactional
	public void deleteProject(Project project) {
		this.projectRepository.delete(project);
	}
	
	@Transactional
	public Project shareProjectWithUser(Project project, User user) {
		project.addMember(user);
		return this.projectRepository.save(project);
	}
	
	@Transactional
	public List<Project> getProjectsSharedWithMe(User user) {
		
		return this.projectRepository.findByMembers(user);
	}

	public Task getTask(Long task_id) {
		Optional<Task> optionalTask = this.taskRepository.findById(task_id);
		return optionalTask.orElse(null);
	}

	public void assegnaTask(Task task, User user) {
		task.setUser(user);
		this.taskRepository.save(task);
	}

	public void completeTask(Task task) {
		task.setCompleted(true);
		this.taskRepository.save(task);
	}

	public void creaNuovoTask(Project project, Task task) {
		task.setCreationTimestamp(LocalDateTime.now());
		project.addTask(task);
		this.taskRepository.save(task);
	}
	
	
}

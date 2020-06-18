package it.uniroma3.projects4you.services;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.projects4you.model.Project;
import it.uniroma3.projects4you.model.User;
import it.uniroma3.projects4you.repository.ProjectRepository;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectRepository projectRepository;
	
	
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
		project.deleteSelf();
		
		this.projectRepository.deleteById(project.getId());
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

	
	
}

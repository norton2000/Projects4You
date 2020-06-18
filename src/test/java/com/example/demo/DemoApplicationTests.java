package com.example.demo;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.uniroma3.projects4you.model.Project;
import it.uniroma3.projects4you.model.User;
import it.uniroma3.projects4you.repository.ProjectRepository;
import it.uniroma3.projects4you.repository.TaskRepository;
import it.uniroma3.projects4you.repository.UserRepository;
import it.uniroma3.projects4you.services.ProjectService;
import it.uniroma3.projects4you.services.UserService;

@SpringBootTest
@RunWith(SpringRunner.class)
class DemoApplicationTests {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private ProjectService projectService;
	
	@Before
	public void deleteAll() {
		System.out.println("Deleting all data...");
		userRepository.deleteAll();
		taskRepository.deleteAll();
		projectRepository.deleteAll();
		System.out.println("Done.");
	}
	
	
	@Test
	void testUpdateUser() {
		/*
		
		Project project1 = new Project("testProject1", "descrizione");
		project1.setOwner(user1Update);
		project1 = projectService.saveProject(project1);
		assertEquals(project1.getOwner(), user1Update);
		assertEquals(project1.getName(), "testProject1");
		
		Project project2 = new Project("testProject2", "descrizione");
		project2.setOwner(user1Update);
		project2 = projectService.saveProject(project2);
		assertEquals(project2.getOwner(), user1Update);
		assertEquals(project2.getName(), "testProject2");
		
		project1 = projectService.shareProjectWithUser(project1, user2);
		List<Project> projects = projectRepository.findByOwner(user1Update);
		assertEquals(projects.size(), 2);
		assertEquals(projects.get(0), project1);
		assertEquals(projects.get(1), project2); 
		
		List<Project> projectVisibleByUser2 = projectRepository.findByMembers(user2);
		assertEquals(projectVisibleByUser2.size(), 1);
		assertEquals(projectVisibleByUser2.get(0), project1);
		
		List<User> project1Members = userRepository.findByVisibleProjects(project1);
		assertEquals(project1Members.size(), 1);
		assertEquals(project1Members.get(0), user2);*/
	}
}

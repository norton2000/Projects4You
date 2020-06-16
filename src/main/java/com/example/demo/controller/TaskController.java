package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.controller.session.SessionData;
import com.example.demo.model.Commento;
import com.example.demo.model.Project;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.services.ProjectService;
import com.example.demo.services.TaskService;
import com.example.demo.services.UserService;
import com.example.demo.validator.TaskValidator;

@Controller
public class TaskController {

	
	@Autowired
	ProjectService projectService;
	@Autowired
	UserService userService;
	@Autowired
	TaskService taskService;
	@Autowired
	TaskValidator taskValidator;
	
	@Autowired
	SessionData sessionData;
	
	
	@GetMapping(value = "/projects/{id}/tasks/add")
	public String startAddNewTask(@PathVariable("id") Long project_id, Model model) {
		model.addAttribute("project", this.projectService.getProject(project_id));
		model.addAttribute("task", new Task());
		return "tasks/formTask";
	}
	
	@PostMapping(value = "/projects/{id}/tasks/add")
	public String addNewTask(@PathVariable("id") Long project_id,
							  @ModelAttribute("task") Task task,
							  BindingResult taskErrors,
							  Model model) 
	{
		Project project = this.projectService.getProject(project_id);
		model.addAttribute("project", project);
		this.taskValidator.validate(task, taskErrors);
		if(taskErrors.hasErrors()) {
			model.addAttribute("task", task);
			return "tasks/formTask";
		}
		task.setCreationTimestamp(LocalDateTime.now());
		project.addTask(task);
		this.projectService.saveProject(project);
		return "redirect:/projects/"+project.getId();
	}
	
	@GetMapping(value = "/projects/{project_id}/tasks/{task_id}/assegna")
	public String getListaAssegna(@PathVariable("project_id") Long project_id,
									@PathVariable("task_id") Long task_id,
									Model model) {
		Project project = this.projectService.getProject(project_id);
		model.addAttribute("project", project);
		User loggedUser = this.sessionData.getLoggedUser();
		List<User> members = userService.getMembers(project);
		Task task = this.projectService.getTask(task_id);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("task", task);
		model.addAttribute("members", members);
		
		return "tasks/assegnaTask";
	}
	
	@RequestMapping(value = "/projects/{project_id}/tasks/{task_id}/assegna/{user_id}")
	public String assegnaTask(@PathVariable("project_id") Long project_id,
								@PathVariable("task_id") Long task_id,
								@PathVariable("user_id") Long user_id)
	{
		Task task = this.projectService.getTask(task_id);
		User user = this.userService.getUser(user_id);
		this.projectService.assegnaTask(task, user);
		return "redirect:/projects/"+project_id;
	}
	
	@RequestMapping(value = "/projects/{project_id}/tasks/{task_id}/completed")
	public String setTaskCompleted(@PathVariable("project_id") Long project_id,
								@PathVariable("task_id") Long task_id)
	{
		Task task = this.projectService.getTask(task_id);
		this.projectService.completeTask(task);
		return "redirect:/projects/"+project_id;
		
	}
	
	@PostMapping(value = "/project/{project_id}/tasks/{task_id}/comment")
	public String leaveComment(@PathVariable("project_id") Long project_id,
								@PathVariable("task_id") Long task_id,
								@ModelAttribute("newComment") Commento commento,
								BindingResult commentError,
								Model model)
	{
		Task task = this.projectService.getTask(task_id);
		User user = this.sessionData.getLoggedUser();
		this.taskService.addComment(task, commento, user);
		
		return "redirect:/projects/"+project_id;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

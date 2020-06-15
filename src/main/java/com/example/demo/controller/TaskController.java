package com.example.demo.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Project;
import com.example.demo.model.Task;
import com.example.demo.services.ProjectService;
import com.example.demo.services.TaskService;
import com.example.demo.validator.TaskValidator;

@Controller
public class TaskController {

	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	TaskService taskService;
	@Autowired
	TaskValidator taskValidator;
	
	
	
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
		System.out.println("here");
		Project project = this.projectService.getProject(project_id);
		model.addAttribute("project", project);
		this.taskValidator.validate(task, taskErrors);
		if(taskErrors.hasErrors()) {
			model.addAttribute("task", task);
			return "formTask";
		}
		task.setCreationTimestamp(LocalDateTime.now());
		project.addTask(task);
		this.projectService.saveProject(project);
		
		return "redirect:/projects/"+project_id;
	}
	
}

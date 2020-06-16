package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Project;
import com.example.demo.model.Tag;
import com.example.demo.services.ProjectService;
import com.example.demo.services.TagService;
import com.example.demo.validator.TagValidator;

@Controller
public class TagController {

	@Autowired
	private TagService tagService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private TagValidator tagValidator;
	
	@GetMapping(value = "/projects/{id}/tags/add")
	public String startAddNewTask(@PathVariable("id") Long project_id, Model model) {
		model.addAttribute("project", this.projectService.getProject(project_id));
		model.addAttribute("tag", new Tag());
		return "tags/formTag";
	}
	
	@PostMapping(value = "/projects/{id}/tags/add")
	public String addNewTask(@PathVariable("id") Long project_id,
							  @ModelAttribute("tag") Tag tag,
							  BindingResult taskErrors,
							  Model model) 
	{
		Project project = this.projectService.getProject(project_id);
		
		if(project == null) {
			return "redirect:/projects";
		}
		
		model.addAttribute("project", project);
		this.tagValidator.validate(tag, taskErrors);
		if(taskErrors.hasErrors()) {
			model.addAttribute("task", tag);
			return "tags/formTag";
		}
		
		
		this.tagService.saveTag(tag, project);
		
		return "redirect:/projects/"+project.getId();
	}
}

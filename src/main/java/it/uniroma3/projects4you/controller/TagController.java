package it.uniroma3.projects4you.controller;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.projects4you.controller.session.SessionData;
import it.uniroma3.projects4you.model.Colore;
import it.uniroma3.projects4you.model.Commento;
import it.uniroma3.projects4you.model.Project;
import it.uniroma3.projects4you.model.Tag;
import it.uniroma3.projects4you.model.Task;
import it.uniroma3.projects4you.services.ProjectService;
import it.uniroma3.projects4you.services.TagService;
import it.uniroma3.projects4you.services.TaskService;
import it.uniroma3.projects4you.validator.TagValidator;

@Controller
public class TagController {

	@Autowired
	private TagService tagService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private TagValidator tagValidator;
	
	@Autowired
	SessionData sessionData;
	
	@GetMapping(value = "/projects/{id}/tags/add")
	public String startAddNewTask(@PathVariable("id") Long project_id, Model model) {
		Project project = this.projectService.getProject(project_id);
		if(project == null) {
			return "redirect:/projects";
		}
		if(!project.getOwner().equals(sessionData.getLoggedUser())) {
			return "redirect:/projects";
		}
		model.addAttribute("project", project);
		model.addAttribute("tag", new Tag());
		model.addAttribute("colori", Colore.allColors());
		
		return "tags/formTag";
	}
	
	@PostMapping(value = "/projects/{id}/tags/add")
	public String addNewTask(@PathVariable("id") Long project_id,
							  @ModelAttribute("tag") Tag tag,
							  BindingResult tagErrors,
							  Model model) 
	{
		Project project = this.projectService.getProject(project_id);
		
		if(project == null) {
			return "redirect:/projects";
		}
		if(!project.getOwner().equals(sessionData.getLoggedUser())) {
			return "redirect:/projects";
		}
		
		model.addAttribute("project", project);
		if(project.containsTag(tag))
			tagErrors.rejectValue("name", "duplicate");
		this.tagValidator.validate(tag, tagErrors);
		if(tagErrors.hasErrors()) {
			model.addAttribute("tag", tag);
			model.addAttribute("colori", Colore.allColors());
			return "tags/formTag";
		}
		
		
		this.tagService.saveTag(tag, project);
		
		return "redirect:/projects/"+project.getId();
	}
	
	@GetMapping(value = "/projects/{project_id}/tags/{tag_id}")
	public String editTask(@PathVariable("project_id") Long project_id,
							@PathVariable("tag_id") Long tag_id,
							Model model) 
	{
		Project project = this.projectService.getProject(project_id);
		Tag tag = this.tagService.getTag(tag_id);
		if(project == null ) {
			return "redirect:/projects";
		}
		if(tag == null) {
			return "redirect:/projects/"+project_id; //TODO Bisognerebbe aggiungere un redirect ad una pagina di errore
		}

		model.addAttribute("newComment", new Commento());
		model.addAttribute("tag", tag);
		model.addAttribute("project", project);
		
		return "tags/tag";
	}
	
	@GetMapping(value = "/projects/{project_id}/tags/{tag_id}/delete")
	public String deleteTask(@PathVariable("project_id") Long project_id,
								@PathVariable("tag_id") Long tag_id,
								Model model)
	{
		Project project = this.projectService.getProject(project_id);
		Tag tag = this.tagService.getTag(tag_id);
		
		if(project == null) {
			return "redirect:/projects";
		}
		if(tag == null) {
			return "redirect:/projects/"+project_id;
		}
		if(!tag.getProject().equals(project) || !project.getOwner().equals(this.sessionData.getLoggedUser())) {
			return "redirect:/projects";
		}
		
		this.tagService.deleteTag(tag);
		
		return "redirect:/projects/"+project_id;
	}
	
	@GetMapping(value = "/projects/{project.id}/tasks/{task.id}/tags/add")
	public String addTagToTask(@PathVariable("project.id") Long project_id,
										@PathVariable("task.id") Long task_id,
										Model model) {
		Project project = this.projectService.getProject(project_id);
		Task task = this.taskService.getTask(task_id);
		if(project == null) {
			return "redirect:/projects";
		}
		if(task == null) {
			return "redirect:/projects/"+project_id;
		}
		if(!project.getOwner().equals(this.sessionData.getLoggedUser())) {
			return "redirect:/projects";
		}
		List<Tag> allTags = new ArrayList<>();
		for(Tag tag : project.getTags()) {
			allTags.add(tag);
		}
		model.addAttribute("task",task);
		model.addAttribute("project", project);
		model.addAttribute("allTags", allTags);
		return "tags/addTagToTask";
	}
	
	@PostMapping(value = "/projects/{project.id}/tasks/{task.id}/tags/add")
	public String removeTagFromTask(@PathVariable("project.id") Long project_id,
										@PathVariable("task.id") Long task_id,
										@ModelAttribute("task") Task newTask,
										Model model) {
		Project project = this.projectService.getProject(project_id);
		Task task = this.taskService.getTask(task_id);
		if(project == null) {
			return "redirect:/projects";
		}
		if(task == null) {
			return "redirect:/projects/"+project_id;
		}
		if(!project.getOwner().equals(this.sessionData.getLoggedUser())) {
			return "redirect:/projects";
		}
		
		task.updateTag(newTask.getTags());
		
		taskService.saveTask(task);
		
		return "redirect:/projects/"+project_id;
	}
	
	
}

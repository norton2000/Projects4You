package it.uniroma3.projects4you.controller;


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

import it.uniroma3.projects4you.controller.session.SessionData;
import it.uniroma3.projects4you.model.Commento;
import it.uniroma3.projects4you.model.Project;
import it.uniroma3.projects4you.model.Task;
import it.uniroma3.projects4you.model.User;
import it.uniroma3.projects4you.services.ProjectService;
import it.uniroma3.projects4you.services.TaskService;
import it.uniroma3.projects4you.services.UserService;
import it.uniroma3.projects4you.validator.TaskValidator;

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
		Project project = this.projectService.getProject(project_id);
		
		if(project ==  null) {
			return "redirect:/projects";
		}
		if(!project.getOwner().equals(this.sessionData.getLoggedUser())) {
			return "redirect:/projects";
		}
		
		model.addAttribute("project", project);
		model.addAttribute("task", new Task());
		return "tasks/formAddTask";
	}
	
	@PostMapping(value = "/projects/{id}/tasks/add")
	public String addNewTask(@PathVariable("id") Long project_id,
							  @ModelAttribute("task") Task task,
							  BindingResult taskErrors,
							  Model model) 
	{
		Project project = this.projectService.getProject(project_id);
		
		if(project == null) {
			return "redirect:/projects";
		}
		if(!project.getOwner().equals(sessionData.getLoggedUser())) {
			return "redirect:/projects";								//TODO Bisognerebbe aggiungere un redirect ad una pagina di errore
		}
		
		model.addAttribute("project", project);
		this.taskValidator.validate(task, taskErrors);
		if(taskErrors.hasErrors()) {
			model.addAttribute("task", task);
			return "tasks/formAddTask";
		}
		
		
		this.taskService.creaNuovoTask(project, task);
		
		return "redirect:/projects/"+project.getId();
	}
	
	@GetMapping(value = "/projects/{project_id}/tasks/{task_id}/assegna")
	public String getListaAssegna(@PathVariable("project_id") Long project_id,
									@PathVariable("task_id") Long task_id,
									Model model) {
		Project project = this.projectService.getProject(project_id);
		
		if(project == null) {
			return "redirect:/projects/"+project_id;  //TODO Bisognerebbe fare un redirect ad una pagina di errore
		}
		if(!project.getOwner().equals(sessionData.getLoggedUser())) {
			return "redirect:/projects";								//TODO Bisognerebbe aggiungere un redirect ad una pagina di errore
		}
		
		model.addAttribute("project", project);
		User loggedUser = this.sessionData.getLoggedUser();
		List<User> members = userService.getMembers(project);
		Task task = this.taskService.getTask(task_id);
		
		if(task == null) {
			return "redirect:/projects/"+project_id; //TODO Bisognerebbe fare un redirect ad una pagina di errore
		}
		
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
		Project project = this.projectService.getProject(project_id);
		if(project == null)
			return "redirect:/projects";
		if(!project.getOwner().equals(sessionData.getLoggedUser())) {
			return "redirect:/projects";								//TODO Bisognerebbe aggiungere un redirect ad una pagina di errore
		}
		
		Task task = this.taskService.getTask(task_id);
		
		User user = this.userService.getUser(user_id);
		
		if(user == null || user == null) {
			return "redirect:/projects/"+project_id; //TODO Bisognerebbe fare un redirect ad una pagina di errore
		}
		
		this.taskService.assegnaTask(task, user);
		return "redirect:/projects/"+project_id;
	}
	
	@GetMapping(value = "/projects/{project_id}/tasks/{task_id}/edit")
	public String editTask(@PathVariable("project_id") Long project_id,
							@PathVariable("task_id") Long task_id,
							Model model) 
	{
		Project project = this.projectService.getProject(project_id);
		Task task = this.taskService.getTask(task_id);
		if(project == null ) {
			return "redirect:/projects";
		}
		if(!project.getOwner().equals(sessionData.getLoggedUser())) {
			return "redirect:/projects";								//TODO Bisognerebbe aggiungere un redirect ad una pagina di errore
		}
		if(task == null) {
			return "redirect:/projects/"+project_id; //TODO Bisognerebbe aggiungere un redirect ad una pagina di errore
		}

		
		model.addAttribute("task", task);
		model.addAttribute("project", project);
		
		return "tasks/formEditTask";
	}
	
	@PostMapping(value = "/projects/{project_id}/tasks/{task_id}/edit")
	public String editTask(@PathVariable("project_id") Long project_id,
							@PathVariable("task_id") Long task_id,
							@ModelAttribute("task") Task task,
							BindingResult taskErrors,
							Model model) {
		Project project = this.projectService.getProject(project_id);
		
		if(project == null) {
			return "redirect:/projects";  //TODO Bisognerebbe aggiungere un redirect ad una pagina di errore
		}
		if(!project.getOwner().equals(sessionData.getLoggedUser())) {
			return "redirect:/projects";								//TODO Bisognerebbe aggiungere un redirect ad una pagina di errore
		}
		
		Task taskDaModificare = this.taskService.getTask(task_id);
		
		if(taskDaModificare == null) {
			return "redirect:/projects/"+project_id; //TODO Bisognerebbe aggiungere un redirect ad una pagina di errore
		}
		
		this.taskValidator.validateEdit(project, taskDaModificare, task, taskErrors);
		
		if(taskErrors.hasErrors()) {
			task.setId(task_id);
			model.addAttribute("project", project);
			return "tasks/formEditTask";
		}
		
		taskDaModificare.setName(task.getName());
		taskDaModificare.setDescription(task.getDescription());
		this.taskService.saveTask(taskDaModificare);
		
		return "redirect:/projects/"+project_id;
	}
	
	
	@GetMapping(value = "/projects/{project_id}/tasks/{task_id}/delete")
	public String deleteTask(@PathVariable("project_id") Long project_id,
								@PathVariable("task_id") Long task_id,
								Model model)
	{
		Project project = this.projectService.getProject(project_id);
		Task task = this.taskService.getTask(task_id);
		
		//TODO Per tutti e tre servirebbe la pagina di errore
		if(project == null) {
			return "redirect:/projects";
		}
		if(task == null) {
			return "redirect:/projects/"+project_id;
		}
		if(!task.getProject().equals(project) || !project.getOwner().equals(this.sessionData.getLoggedUser())) {
			return "redirect:/projects";
		}
		
		
		this.taskService.deleteTask(project, task);
		
		return "redirect:/projects/"+project_id;
	}
	
	
	@RequestMapping(value = "/projects/{project_id}/tasks/{task_id}/completed")
	public String setTaskCompleted(@PathVariable("project_id") Long project_id,
								@PathVariable("task_id") Long task_id)
	{
		Task task = this.taskService.getTask(task_id);
		
		if(task == null) {
			return "redirect:/projects/"+project_id; //TODO Bisognerebbe fare un redirect ad una pagina di errore
		}
		
		this.taskService.setCompletedTask(task);
		return "redirect:/projects/"+project_id;
		
	}
	
	@PostMapping(value = "/projects/{project_id}/tasks/{task_id}/comment")
	public String leaveComment(@PathVariable("project_id") Long project_id,
								@PathVariable("task_id") Long task_id,
								@ModelAttribute("newComment") Commento commento,
								BindingResult commentError,
								Model model)
	{
		Task task = this.taskService.getTask(task_id);
		
		if(task == null) {
			return "redirect:/projects/"+project_id; //TODO Bisognerebbe fare un redirect ad una pagina di errore
		}
		
		User user = this.sessionData.getLoggedUser();
		this.taskService.addComment(task, commento, user);
		
		return "redirect:/projects/"+project_id;
	}

	
	@GetMapping(value = "/projects/{project_id}/tasks/{task_id}")
	public String accediTask(@PathVariable("project_id") Long project_id,
							@PathVariable("task_id") Long task_id,
							Model model)
	{
		return "redirect:/projects/"+project_id;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.controller.session.SessionData;
import com.example.demo.model.Commento;
import com.example.demo.model.Project;
import com.example.demo.model.User;
import com.example.demo.services.ProjectService;
import com.example.demo.services.UserService;
import com.example.demo.validator.ProjectValidator;

@Controller
public class ProjectController {

	@Autowired
	ProjectService projectService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ProjectValidator projectValidator;
	
	@Autowired
	SessionData sessionData;
	
	@RequestMapping(value = "/projects")
	public String onProjects() {
		return "redirect:/projects/myProjects";
	}
	
	@RequestMapping(value = { "/projects/myProjects" }, method = RequestMethod.GET)
	public String myOwenedProjects(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> projectList = projectService.retrieveProjectsOwnedBy(loggedUser);
		model.addAttribute("loggedUser",loggedUser);
		model.addAttribute("projectsList", projectList);
		return "projectsList/myOwnedProjects";
	}
	
	@RequestMapping(value = {"/projects/myProjects/{projectId}"}, method = RequestMethod.GET)
	public String visualizzaProjectOwned(Model model, @PathVariable Long projectId) {
		User loggedUser = sessionData.getLoggedUser();
		Project project = projectService.getProject(projectId);
		if(project == null)
			return "redirect:/projects/myProjects";
		
		List<User> members = userService.getMembers(project);
		if(!project.getOwner().equals(loggedUser) && !members.contains(loggedUser))
			return "redirect:/projects/myProjects";
		
		model.addAttribute("loggedUser", loggedUser);
		this.preparaPerVistaProgetto(model, project);
		
		return "projectOwned";
	}
	
	@RequestMapping(value = "/projects/{project_id}")
	public String onGetMyProject(@PathVariable("project_id") Long project_id) 
	{
		Project project = this.projectService.getProject(project_id);
		
		if(project == null)
			return "redirect:/projects/myProjects";  //TODO Bisognerebbe fare un redirect ad una pagina di errore
		
		User loggedUser = this.sessionData.getLoggedUser();
		
		if(project.getOwner().equals(loggedUser)) {
			return "redirect:/projects/myProjects/"+project_id;
		}else if(project.getMembers().contains(loggedUser)) {
			return "redirect:/projects/sharedWithMe/"+project_id;
		}
		return "redirect:/projects/myProjects";
	}
	
	@RequestMapping(value = {"/projects/add"}, method = RequestMethod.GET)
	public String createProjectForm(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectForm", new Project());
		return "projects/addProject";
	}
	
	@RequestMapping(value = {"/projects/add"}, method = RequestMethod.POST)
	public String createPorject(@Valid @ModelAttribute("projectForm") Project project,
								BindingResult projectBindingResult, Model model) {
		User loggedUser = sessionData.getLoggedUser();
		
		projectValidator.validate(project, projectBindingResult);
		if(!projectBindingResult.hasErrors()) {
			project.setOwner(loggedUser);
			this.projectService.saveProject(project);
			return "redirect:/projects/" + project.getId();
		}
		model.addAttribute("loggedUser", loggedUser);
		return "projects/addProject";
	}
	
	@GetMapping(value = "/projects/{project_id}/delete")
	public String deleteProject(@PathVariable("project_id") Long project_id,
								Model model)
	{
		Project project = this.projectService.getProject(project_id);
		
		//TODO Ci sarebbe da mettere una pagina di errore
		if(project == null)
			return "redirect:/projects";
		if(!project.getOwner().equals(this.sessionData.getLoggedUser())) {
			return "redirect:/projects";
		}
		
		this.projectService.deleteProject(project);
		
		
		return "redirect:/projects";
	}
	
	@GetMapping(value = "projects/{project_id}/edit")
	public String editProject(@PathVariable("project_id") Long project_id,
								Model model)
	{
		Project project = this.projectService.getProject(project_id);
		if(project == null) {
			return "redirect:/projects";
		}
		if(!project.getOwner().equals(this.sessionData.getLoggedUser())) {
			return "redirect:/projects";
		}
		
		model.addAttribute("projectForm", project);
		
		return "projects/editProject";
	}
	
	@PostMapping(value = "/projects/{project_id}/edit")
	public String editProject(@PathVariable("project_id") Long project_id,
								@ModelAttribute("projectForm") Project projectModificato,
								BindingResult errors,
								Model model)
	{
		Project project = this.projectService.getProject(project_id);
		
		if(project == null) {
			return "redirect:/projects";
		}
		if(!project.getOwner().equals(this.sessionData.getLoggedUser())) {
			return "redirect:/projects";
		}
		
		this.projectValidator.validate(projectModificato, errors);
		if(errors.hasErrors()) {
			projectModificato.setId(project_id);
			return "projects/editProject";
		}
		project.setName(projectModificato.getName());
		project.setDescription(projectModificato.getDescription());
		this.projectService.saveProject(project);
		return "redirect:/projects";
	}

	
	
	@PostMapping(value = {"projects/{id}/share"})
	public String shareProject(@PathVariable("id") Long id_progetto,
							  @ModelAttribute("userToShare") User userToShare,
							  BindingResult errors,
							  Model model) {
		
		//Prendo i dati che serviranno, e ripreparo i dati di base per la pagina
		User loggedUser = this.sessionData.getLoggedUser();
		Project project = this.projectService.getProject(id_progetto);
		
		if(project == null) {
			return "redirect:/projects/"+id_progetto;  //TODO Bisognerebbe fare un redirect ad una pagina di errore
		}
		
		List<User> members = userService.getMembers(project);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("project", project);
		model.addAttribute("members", members);
		model.addAttribute("newComment", new Commento());
	
		User user = this.userService.getUser(userToShare.getNickname());
		if(user == null) {
			errors.rejectValue("nickname", "notExists");
			return "projectOwned";
		}
		
		//Se sto cercando di condividere con me stesso o se chi cerco è già membro del progetto
		if(user.equals(loggedUser) || members.contains(user)) {
			errors.rejectValue("nickname", "alreadyMember");
			return "projectOwned";
		}
		project.addMember(user);
		this.projectService.saveProject(project);
		
		return "redirect:/projects/"+id_progetto;
	}
	
	@GetMapping(value = "/projects/sharedWithMe")
	public String getListaSharedWithMe(Model model) {
		
		User loggedUser = this.sessionData.getLoggedUser();
		List<Project> projectsShared = this.projectService.getProjectsSharedWithMe(loggedUser);
		
		model.addAttribute("projectsList", projectsShared);
		return "projectsList/sharedWithMe";
	}
	
	@GetMapping(value = "projects/sharedWithMe/{project_id}")
	public String getProjectShared(@PathVariable(value = "project_id") Long project_id,
									Model model) {
		
		Project project = this.projectService.getProject(project_id);
		
		if(project == null) {
			return "redirect:/projects/"+project_id;  //TODO Bisognerebbe fare un redirect ad una pagina di errore
		}
		
		User loggedUser = this.sessionData.getLoggedUser();
		
		if(project.getMembers().contains(loggedUser))
			this.preparaPerVistaProgetto(model, project);
		
		return "fragments/project";
	}
	
	
	private void preparaPerVistaProgetto(Model model, Project project) {
		
		List<User> members = userService.getMembers(project);
		System.out.println("================================");
		System.out.println(project.getTasks().size());
		System.out.println("================================");
		model.addAttribute("project", project);
		model.addAttribute("members", members);
		model.addAttribute("userToShare", new User()); //evenutale campo in cui l'utente potrà inserire lo username di qualcuno con cui vuole condividere il progetto
		model.addAttribute("newComment", new Commento());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

package it.uniroma3.projects4you.controller;


import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.projects4you.controller.session.SessionData;
import it.uniroma3.projects4you.model.Credentials;
import it.uniroma3.projects4you.model.User;
import it.uniroma3.projects4you.services.CredentialsService;
import it.uniroma3.projects4you.services.UserService;
import it.uniroma3.projects4you.validator.UserValidator;

@Controller
public class UserController {

	@Autowired
	private SessionData sessionData;

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserValidator userValidator;

	@RequestMapping(value = { "/home" }, method = RequestMethod.GET)
	public String home (Model model) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("user", loggedUser);
		return "home";
	}

	@RequestMapping(value = { "/admin" }, method = RequestMethod.GET)
	public String admin (Model model) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("user", loggedUser);
		return "admin";
	}

	@RequestMapping(value = { "/users/me" }, method = RequestMethod.GET)
	public String me (Model model) {
		User loggedUser = sessionData.getLoggedUser();
		Credentials credentials = sessionData.getLoggedCredentials();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("credentials", credentials);
		return "userProfile";
	}

	@RequestMapping(value = { "/admin/users" }, method = RequestMethod.GET)
	public String usersList(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Credentials> allCredentials = this.credentialsService.getAllCredentials();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("allCredentials", allCredentials);
		return "allUsers";
	}

	@RequestMapping(value = {"/admin/users/{username}/delete"}, method = RequestMethod.POST)
	public String removeUser(Model model, @PathVariable String username) {
		Credentials credentials = this.credentialsService.getCredentials(username);
		if(credentials == null) {
			return "redirect:/admin/users";
		}
		String redirecting = null;
		User user = credentials.getUser();
		if(user.equals(this.sessionData.getLoggedUser())) {
			SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
			redirecting = "/index";
		}else {
			redirecting = "redirect:/admin/users";
		}
		user.deleteSelf();
		this.credentialsService.deleteCredentials(username);
		
		return redirecting;
	}

    @GetMapping(value = {"/users/modify"})
    public String getModifyUserPage(Model model) {
    	User userLoggato = this.sessionData.getLoggedUser();
    	model.addAttribute("userForm", userLoggato); 
    	return "modificaUser";
    }

    @PostMapping(value = {"/users/modify"})
    public String modifyUser(@Valid @ModelAttribute("userForm") User newValues,
    						BindingResult errors,
    						Model model)
    {
    	User userLoggato = this.sessionData.getLoggedUser();
    	this.userValidator.validateUpdate(newValues, errors);
    	if(!errors.hasErrors()) {
    		userLoggato.setFirstname(newValues.getFirstname());
    		userLoggato.setLastname(newValues.getLastname());
    		userLoggato.setNickname(newValues.getNickname());
    		this.userService.saveUser(userLoggato);
    		model.addAttribute("credentials", this.sessionData.getLoggedCredentials());
    		model.addAttribute("loggedUser", userLoggato);
    		return "redirect:/users/me";
    	}
    	return "modificaUser";
    }
}

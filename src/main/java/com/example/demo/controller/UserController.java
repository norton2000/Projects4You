package com.example.demo.controller;



import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.example.demo.model.Credentials;
import com.example.demo.model.User;
import com.example.demo.services.CredentialsService;
import com.example.demo.services.UserService;
import com.example.demo.validator.UserValidator;


/**
 * The UserController handles all interactions involving User data.
 */
@Controller
public class UserController {


  

    @Autowired
    UserValidator userValidator;
    
    @Autowired
    UserService userService;


    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired
    SessionData sessionData;
    
    @Autowired
    CredentialsService credentialsService;
    
    


    /**
     * This method is called when a GET request is sent by the user to URL "/users/user_id".
     * This method prepares and dispatches the User registration view.
     *
     * @param model the Request model
     * @return the name of the target view, that in this case is "register"
     */
    @RequestMapping(value = { "/home" }, method = RequestMethod.GET)
    public String home(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        model.addAttribute("user", loggedUser);
        return "home";
    }


    /**
     * This method is called when a GET request is sent by the user to URL "/users/user_id".
     * This method prepares and dispatches the User registration view.
     *
     * @param model the Request model
     * @return the name of the target view, that in this case is "register"
     */
    @RequestMapping(value = { "/users/me" }, method = RequestMethod.GET)
    public String me(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        Credentials credentials = sessionData.getLoggedCredentials();
        System.out.println(credentials.getPassword());
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("credentials", credentials);


        return "userProfile";
    }
    
    @GetMapping(value = {"/users/modify"})
    public String getModifyUserPage(Model model) {
    	User userLoggato = this.sessionData.getLoggedUser();
    	User user = new User(userLoggato.getFirstname()	, userLoggato.getLastname());
    	model.addAttribute("userForm", user); 	
    	return "modificaUser";
    }
    
    @PostMapping(value = {"/users/modify"})
    public String modifyUser(@Valid @ModelAttribute("userForm") User newValues,
    						BindingResult errors,
    						Model model) 
    {
    	User userLoggato = this.sessionData.getLoggedUser();
    	this.userValidator.validate(newValues, errors);
    	if(!errors.hasErrors()) {
    		userLoggato.setFirstname(newValues.getFirstname());
    		userLoggato.setLastname(newValues.getLastname());
    		userLoggato.setNickname(newValues.getNickname());
    		this.userService.saveUser(userLoggato);
    		model.addAttribute("credentials", this.sessionData.getLoggedCredentials());
    		model.addAttribute("loggedUser", userLoggato);
    		return "userProfile";
    	}
    	return "modificaUser";
    }


    /**
     * This method is called when a GET request is sent by the user to URL "/users/user_id".
     * This method prepares and dispatches the User registration view.
     *
     * @param model the Request model
     * @return the name of the target view, that in this case is "register"
     */
    @RequestMapping(value = { "/admin" }, method = RequestMethod.GET)
    public String admin(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        model.addAttribute("user", loggedUser);
        return "admin";
    }
    
    @RequestMapping(value="/admin/users",method=RequestMethod.GET)
    public String usersList(Model model) {
        User loggedUser=sessionData.getLoggedUser();
        List<Credentials> allCredentials=this.credentialsService.getAllCredentials();
        model.addAttribute("loggedUser",loggedUser);
        model.addAttribute("allCredentials",allCredentials);
        return "allUsers";
    }


    
//    this  method is called when a POST request is sent by the user to URL "/admin/users/{username}/delete".
//    this method deletes the user whose credentials are identified by the passed username{username}.
//    It then redirects the admin to the page listing all users.
//    @param model the Request model
//    @return the name of the target view,that in this case is "register"
    @RequestMapping(value="/admin/users/{username}/delete",method=RequestMethod.POST)
    public String removeUser(Model model,@PathVariable("username") String username) {
    	System.out.println("Adesso provo ad eliminare il signor " + username);
        this.credentialsService.deleteCredentials(username);
        System.out.println("Ok, l'ho eliminato e ti ritorno la vista");
        return "redirect:/admin/users";
    }
}

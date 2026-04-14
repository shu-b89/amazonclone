package com.example.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.enity.User;
import com.example.demo.services.UserService;

@Controller
public class Mycontroller 
{
	@Autowired
	private UserService userserv;
	
    @GetMapping("/regPage")
	public String openRegPage(Model model)
	{
    	model.addAttribute("user",new User());
		return "register";
	}
	@PostMapping("/regForm")
    public String submitRegform(@ModelAttribute("user") User user, Model model)
    {
    	boolean status=userserv.registerUser(user);
    	if(status)
    	{
    		model.addAttribute("Success","User register sucessfully!!!");
    	}
    	else {
    		model.addAttribute("errorMsg","user Failed due to some issue");
    	}
		return "register";
    	
    }
	
}

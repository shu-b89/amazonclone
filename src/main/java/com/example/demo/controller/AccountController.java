package com.example.demo.controller;

import com.example.demo.enity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller

public class AccountController {
	@GetMapping("/account")
	public String account(HttpSession session, Model model, RedirectAttributes redirectAttributes) {

	    User loggedInUser = (User) session.getAttribute("loggedInUser");

	    if (loggedInUser == null) {
	        redirectAttributes.addFlashAttribute("error", "Please login to continue");
	        return "redirect:/login";
	    }

	    model.addAttribute("user", loggedInUser);
	    return "account";

	    
	    
	}


}

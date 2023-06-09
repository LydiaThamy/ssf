package vttp2023.batch3.ssf.frontcontroller.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import vttp2023.batch3.ssf.frontcontroller.model.Login;
import vttp2023.batch3.ssf.frontcontroller.services.AuthenticationService;

@Controller
@RequestMapping("/protected")
public class ProtectedController {
	
	// TODO Task 5
	// Write a controller to protect resources rooted under /protected

	@Autowired
	private AuthenticationService service;

	@GetMapping("/view1.html")
	public String getPicture(Model model, HttpSession session) {

		Login login = (Login) session.getAttribute("login");
		session.setAttribute("login", login);
		
		// check if user is not authenticated
		if (login.isAuthenticated() == false) {
			model.addAttribute("login", new Login());
			return "view0";
		}

		return "view1";
	}
}

package vttp2023.batch3.ssf.frontcontroller.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp2023.batch3.ssf.frontcontroller.model.Login;
import vttp2023.batch3.ssf.frontcontroller.services.AuthenticationService;

@Controller
public class FrontController {

	// TODO: Task 2, Task 3, Task 4, Task 6

	@Autowired
	private AuthenticationService service;

	@GetMapping("/")
	public String getLoginPage(Model model, HttpSession session) {

		Login login = (Login) session.getAttribute("login");

		// if person has not logged in before
		if (login == null) {
			model.addAttribute("login", new Login());
			return "view0";
		}

		// if person has logged in before
		// if person has been authenticated --> pressed logout button
		boolean loggedOut = login.isAuthenticated();
		if (loggedOut == true) {
			session.invalidate();
			model.addAttribute("login", new Login());
			return "view0";
		}

		// if person has not been authenticated
		// if person has attempted login for more than 3 times
		if (login.getAttempts() > 3) {

			service.disableUser(login.getUsername());

			session.invalidate();
			model.addAttribute("username", login.getUsername());

			return "view2";
		}

		// if person has not attempted more than 3 times
		login.setCaptcha();
		model.addAttribute("login", login);
		return "view0";
	}

	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
	public String tryLogin(
			@ModelAttribute("login") @Valid Login login,
			BindingResult result,
			HttpSession session,
			Model model) throws Exception {

		// if username and password has errors
		if (result.hasErrors()) {
			System.out.println(login);
			model.addAttribute("login", login);
			return "view0";
		}

		login.setAttempts(1);

		// if captcha is invalid
		if (login.getCaptcha() != null && login.getCorrectAnswer() != login.getUserAnswer()) {
			login.setCaptcha();
			model.addAttribute("login", login);
			return "view0";
		}

		// authenticate user
		String authResult = service.authenticate(login.getUsername(), login.getPassword());
		// if valid
		if (authResult.equals("created")) {
			login.isAuthenticated();
			session.setAttribute("login", login);
			// go to protected controller
			return "view1";
		}

		if (authResult.equals("bad request")) {
			result.addError(new ObjectError("message", "Invalid payload"));
		} else if (authResult.equals("unauthorized")) {
			result.addError(new ObjectError("message", "Incorrect username and/or password"));
		} else {
			result.addError(new ObjectError("message", "Unknown error"));
		}

		// if invalid
		login.setCaptcha();
		model.addAttribute("login", login);
		return "view0";
	}

}

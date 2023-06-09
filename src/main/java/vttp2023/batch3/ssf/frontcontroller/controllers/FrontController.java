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

	@Autowired
	private ProtectedController protectedController;

	@GetMapping("/")
	public String getLoginPage(Model model, HttpSession session) {

		// session.invalidate();
		// Login login = (Login) session.getAttribute("login");

		// // if person has not logged in before
		// if (login == null) {
		// model.addAttribute("login", new Login());
		// return "view0";
		// }

		// // if person has been authenticated --> pressed logout button
		// boolean loggedOut = login.isAuthenticated();
		// if (loggedOut == true) {

		// model.addAttribute("login", new Login());
		// return "view0";
		// }

		// // if person has not attempted more than 3 times
		// login.setCaptcha();

		session.setAttribute("login", new Login());
		System.out.println(((Login) session.getAttribute("login")).getAttempts());
		model.addAttribute("login", new Login());
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
			// session.setAttribute("login", login);
			System.out.println(login.getAttempts());
			model.addAttribute("login", login);
			return "view0";
		}

		login.setAttempts(1);
		System.out.println(login.getAttempts());

		// if captcha is invalid
		if (login.getCaptcha() != null && login.getCorrectAnswer() != login.getUserAnswer()) {
			System.out.println("Captcha present and answer is wrong...");
			login.setCaptcha();
			model.addAttribute("login", login);
			session.setAttribute("login", login);
			return "view0";
		}

		// if person has attempted login for more than 3 times
		if (login.getAttempts() > 3) {

			service.disableUser(login.getUsername());
			session.invalidate();
			model.addAttribute("username", login.getUsername());
			return "view2";
		}

		// authenticate user
		String authResult = service.authenticate(login.getUsername(), login.getPassword());

		// if locked
		if (authResult.equals("locked")) {
			model.addAttribute("username", login.getUsername());
			session.invalidate();
			return "view2";
		}

		// if valid
		if (authResult.equals("created")) {

			login.setAuthenticated();
			// login.setAttempts(0);
			session.setAttribute("login", login);
			// go to protected controller
			return protectedController.getPicture(model, session);
		}

		// if invalid
		// add error to page
		if (authResult.equals("bad request")) {
			System.out.println("bad request...");
			result.addError(new ObjectError("message", "Invalid payload"));
		} else if (authResult.equals("unauthorized")) {
			System.out.println("unauthorized...");
			result.addError(new ObjectError("message", "Incorrect username and/or password"));
		} else {
			System.out.println("unknown error...");
			result.addError(new ObjectError("message", "Unknown error"));
		}
		
		login.setCaptcha();
		model.addAttribute("login", login);
		session.setAttribute("login", login);

		return "view0";
	}

}

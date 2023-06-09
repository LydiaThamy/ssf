package vttp2023.batch3.ssf.frontcontroller.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import vttp2023.batch3.ssf.frontcontroller.services.AuthenticationService;

@Controller
public class ProtectedController {
	
	// TODO Task 5
	// Write a controller to protect resources rooted under /protected

	@Autowired
	private AuthenticationService service;
}

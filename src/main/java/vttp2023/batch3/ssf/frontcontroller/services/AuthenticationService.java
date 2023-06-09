package vttp2023.batch3.ssf.frontcontroller.services;

import java.io.StringReader;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2023.batch3.ssf.frontcontroller.respositories.AuthenticationRepository;

@Service
public class AuthenticationService {

	@Autowired
	private AuthenticationRepository repository;

	@Value("${ssf.authentication.api.url}")
	private String apiUrl;

	// TODO: Task 2
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write the authentication method in here
	public String authenticate(String username, String password) throws Exception {

		// create json body
		JsonObject json = Json.createObjectBuilder()
				.add("username", username)
				.add("password", password)
				.build();

		// create request entity
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		System.out.println(apiUrl);

		RequestEntity<String> req = RequestEntity
				.post(apiUrl)
				.headers(headers)
				.body(json.toString(), String.class);

		RestTemplate template = new RestTemplate();

		ResponseEntity<String> response = template.exchange(req, String.class);

		System.out.println("Status Code: " + response.getStatusCode());

		// evaluate response entity
		if (response.getStatusCode().value() == 201) {
			return "created";
		}

		return "unknown";

	}

	// TODO: Task 3
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to disable a user account for 30 mins
	public void disableUser(String username) {
	}

	// TODO: Task 5
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to check if a given user's login has been disabled
	public boolean isLocked(String username) {
		return false;
	}
}

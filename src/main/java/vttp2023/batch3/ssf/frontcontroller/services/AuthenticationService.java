package vttp2023.batch3.ssf.frontcontroller.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
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

		System.out.println("validating login...");

		// check if user is locked
		if (isLocked(username)) {
			System.out.println("username is locked...");
			return "locked";
		}

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
				.contentType(MediaType.APPLICATION_JSON)
				// .accept(MediaType.APPLICATION_JSON)
				.body(json.toString(), String.class);

		RestTemplate template = new RestTemplate();
		ResponseEntity<String> response = template.exchange(req, String.class);

		// evaluate response entity
		// boolean status = response.getStatusCode().is1xxInformational();
		// System.out.println("Status Code: " + status);

		// if (response.getStatusCode().is1xxInformational()) {
		// 	return "created";
		// }

		// if (response.getStatusCode().is4xxClientError()) {
		// 	return "bad request";
		// }

		// if (response.getStatusCode().is4xxClientError()) {
		// 	return "unauthorized";
		// }
		// evaluate response entity
		int status = response.getStatusCode().value();
		System.out.println("Status Code: " + status);

		if (status == 201) {
			return "created";
		}

		if (status == 400) {
			return "bad request";
		}

		if (status == 401) {
			return "unauthorized";
		}

		return "unknown";
	}

	// TODO: Task 3
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to disable a user account for 30 mins
	public void disableUser(String username) {
		System.out.println("Username disabled due to mulitple log ins...");
		repository.disableUser(username);
	}

	// TODO: Task 5
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to check if a given user's login has been disabled
	public boolean isLocked(String username) {
		return repository.isLocked(username);
	}
}

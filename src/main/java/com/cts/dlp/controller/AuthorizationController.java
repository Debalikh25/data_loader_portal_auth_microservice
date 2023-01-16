package com.cts.dlp.controller;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.dlp.dao.JwtExpired;
import com.cts.dlp.dao.JwtTokenDAO;
import com.cts.dlp.dao.LoginDAO;
import com.cts.dlp.dao.MessageDAO;
import com.cts.dlp.dao.TokenExpiryDateDAO;
import com.cts.dlp.dao.UsernameDAO;
import com.cts.dlp.entities.Login;
import com.cts.dlp.services.AuthorizationService;

@RestController
@CrossOrigin()
@RequestMapping("/api/v1")
public class AuthorizationController {

	@Autowired
	private AuthorizationService service;

	private MessageDAO message = new MessageDAO();

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody() LoginDAO login) throws Exception {

		Login user = this.service.userExist(login);

		if (user == null) {
			message.setMessage("Admin Not Registered");
			return new ResponseEntity<>(message, HttpStatus.OK);
		}

		if (!(login.getPassword().equals(user.getPassword()))) {
			message.setMessage("Password is Incorrect");
			return new ResponseEntity<>(message, HttpStatus.OK);
		}

		JwtTokenDAO token = this.service.createToken(login);

		return new ResponseEntity<>(token, HttpStatus.OK);
	}
	
	

	@GetMapping("/token/expired")
	public JwtExpired tokenExpired(@RequestHeader(name = "auth", required = false) String header) {
           
		  System.out.println("Method Executed");
		     
		if (header == null) {
			return null;
		}

		JwtExpired expired = this.service.tokenExpired(header);

		return expired;
	}
	
	
	

	@GetMapping("/token/expiry")
	public ResponseEntity<?> tokenExpiry(@RequestHeader("auth") String header) {

		Date date = this.service.getTokenExpiration(header);

		TokenExpiryDateDAO date1 = new TokenExpiryDateDAO();
		date1.setDate(date);

		return new ResponseEntity<>(date1, HttpStatus.OK);

	}
	
	

	@GetMapping("/token/username")
	public ResponseEntity<?> getUsername(@RequestHeader("auth") String header) {

		UsernameDAO username = this.service.getUsernameFromToken(header);

		return new ResponseEntity<>(username, HttpStatus.OK);

	}

}

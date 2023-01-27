package com.cts.dlp.controller;

import java.util.Set;
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
import com.cts.dlp.entities.Login;
import com.cts.dlp.entities.Role;
import com.cts.dlp.helpers.StringConstants;
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
			message.setMessage(StringConstants.ADMIN_NOT_REGISTERED);
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}

		Set<Role> roles = user.getRoles();
		if (roles.size() == 0) {
			message.setMessage(StringConstants.ADMIN_NOT_REGISTERED);
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}

		for (Role r : roles) {
			if (!(r.getName().equals(StringConstants.ADMIN_USER))) {
				message.setMessage(StringConstants.NOT_AN_ADMIN);
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
		}

		if (!(login.getPassword().equals(user.getPassword()))) {
			message.setMessage(StringConstants.WRONG_PASSWORD);
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}

		JwtTokenDAO token = this.service.createToken(login);

		return new ResponseEntity<>(token, HttpStatus.OK);
	}

	@GetMapping("/token/expired")
	public JwtExpired tokenExpired(@RequestHeader(name = "auth", required = false) String header) {

		if (header == null) {
			return null;
		}

		JwtExpired expired = this.service.tokenExpired(header);

		return expired;
	}

}

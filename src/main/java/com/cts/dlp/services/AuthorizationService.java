package com.cts.dlp.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.cts.dlp.dao.JwtExpired;
import com.cts.dlp.dao.JwtTokenDAO;
import com.cts.dlp.dao.LoginDAO;
import com.cts.dlp.entities.Login;
import com.cts.dlp.repositories.LoginRepository;
import com.cts.dlp.utils.JwtUtil;

@Service
public class AuthorizationService {

	@Autowired
	private AuthenticationManager auth;

	@Autowired
	private JwtUtil jwtutil;

	@Autowired
	private CustomUserDetailsService service;

	@Autowired
	private LoginRepository repo;

	private JwtExpired jwtExpired = new JwtExpired();
	
	

	// Generate Token Upon Login
	public JwtTokenDAO createToken(LoginDAO login) throws Exception {

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login.getUsername(),
				login.getPassword());

		auth.authenticate(token);

		UserDetails user = this.service.loadUserByUsername(login.getUsername());

		String tokenString = this.jwtutil.generateToken(user);

		JwtTokenDAO tkn = new JwtTokenDAO(tokenString);

		return tkn;

	}

	// Admin Exist or Not
	public Login userExist(LoginDAO login) {

		Login user = this.repo.findByUsername(login.getUsername());
		return user;

	}

	// Check whether token is valid
	public JwtExpired tokenExpired(String header) {
	      
		    String token = header.substring(7);
		     
		    Boolean expired = this.jwtutil.expired(token);
		    if(expired){
		    	
		    this.jwtExpired.setExpired(true);
		      return this.jwtExpired;
		}
		    
		    this.jwtExpired.setExpired(false);
		    return this.jwtExpired;
	}
}

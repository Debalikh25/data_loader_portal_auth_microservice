package com.cts.dlp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cts.dlp.entities.Login;
import com.cts.dlp.entities.Role;
import com.cts.dlp.repositories.LoginRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private LoginRepository repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		  Login login = this.repo.findByUsername(username);
		  if(login == null){
			  throw new UsernameNotFoundException(username+" not found");
		  }
		  
		  Set<Role> roles = login.getRoles();
		  
		  List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		   for(Role r : roles){
			   list.add(new SimpleGrantedAuthority(r.getName()));
		   }
		  
		  User user = new User(login.getUsername() , login.getPassword() , list);
		  
		return user;
	}

}

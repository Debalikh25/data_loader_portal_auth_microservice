package com.cts.dlp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.dlp.entities.Login;

public interface LoginRepository extends JpaRepository<Login, Long> {
	
	 Login findByUsername(String username);
	 
	 

}

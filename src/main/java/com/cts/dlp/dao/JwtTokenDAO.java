package com.cts.dlp.dao;

public class JwtTokenDAO {
	
	private String token;
	
	public JwtTokenDAO(String tkn){
		this.token = tkn;
	}
	
	public void setToken(String tkn){
		this.token = tkn;
	}
	
	public String getToken(){
		return this.token;
	}

}

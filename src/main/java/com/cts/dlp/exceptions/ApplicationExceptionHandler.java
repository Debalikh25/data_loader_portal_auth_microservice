package com.cts.dlp.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import com.cts.dlp.dao.ErrorDAO;

import io.jsonwebtoken.MalformedJwtException;

@ControllerAdvice
public class ApplicationExceptionHandler {

	private ErrorDAO error = new ErrorDAO();

	@ExceptionHandler(MalformedJwtException.class)
	public ErrorDAO malformedJwt(MalformedJwtException ex) {

		error.setError("Invalid Token");
		return error;

	}

}

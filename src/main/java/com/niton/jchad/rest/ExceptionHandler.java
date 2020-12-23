package com.niton.jchad.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(value = HttpClientErrorException.class)
	public ResponseEntity<Object> exception(HttpClientErrorException exception) {
		return new ResponseEntity<>(exception.getStatusCode());
	}
}

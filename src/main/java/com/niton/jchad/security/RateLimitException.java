package com.niton.jchad.security;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RateLimitException extends Exception{
	public RateLimitException(String message) {
		super(message);
	}
}

package com.niton.jchad.rest.model;

import com.niton.login.LoginResult;
import lombok.*;

@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
	private LoginResult result;
	private String      sessionID;
}

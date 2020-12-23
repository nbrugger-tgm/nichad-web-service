package com.niton.jchad.security;

import com.niton.jauth.AccountManager;
import com.niton.jchad.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SessionHandler implements HandlerInterceptor{
	public final static String USER_ID = "uid",AUTHENTICATED = "authed";

	private AccountManager<User,HttpServletRequest> manager;

	@Autowired
	public SessionHandler(DatabaseAuthManager dbAuth) {
		manager = new AccountManager<>(dbAuth);
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
	                         HttpServletResponse response,
	                         Object handler) {
		User    user = manager.getAuthentication(request);
		boolean authed = user != null;
		request.setAttribute(AUTHENTICATED,authed);
		if(authed)
			request.setAttribute(USER_ID, user.getId());
		return true;
	}

}

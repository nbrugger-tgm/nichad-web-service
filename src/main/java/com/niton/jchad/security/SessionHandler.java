package com.niton.jchad.security;

import com.niton.jauth.AccountManager;
import com.niton.jchad.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
public class SessionHandler implements HandlerInterceptor {
	public final static String USER_ID = "uid", AUTHENTICATED = "authed";

	public static AccountManager<User, HttpServletRequest> manager;
	private final DatabaseAuthManager dbAuth;

	@Autowired
	public SessionHandler(DatabaseAuthManager dbAuth) {
		System.out.println("Create Session handler");
		if(manager == null)
			manager= new AccountManager<>(dbAuth);
		this.dbAuth = dbAuth;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
	                         HttpServletResponse response,
	                                            Object handler) {
		User    user = null;
		if(dbAuth.getContextAuthInfo(request) != null && dbAuth.getContextAuthInfo(request).length() != 0)
		   user = manager.getAuthentication(request);
		boolean authed = user != null;
		request.setAttribute(AUTHENTICATED, authed);
		if (authed) {
			request.setAttribute(USER_ID, user.getId());
		}
		return true;
	}

}

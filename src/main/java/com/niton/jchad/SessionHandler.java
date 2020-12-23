package com.niton.jchad;

import com.niton.jauth.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SessionHandler implements HandlerInterceptor{
	public final static String USER_ID = "uid",AUTHENTICATED = "authed";

	private AccountManager<String,HttpServletRequest> manager;

	@Autowired
	public SessionHandler(DatabaseAuthManager dbAuth) {
		manager = new AccountManager<>(dbAuth);
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
	                         HttpServletResponse response,
	                         Object handler) throws Exception {
		String userId = manager.getAuthentication(request);
		boolean authed = userId != null;
		request.setAttribute(AUTHENTICATED,authed);
		if(authed)
			request.setAttribute(USER_ID, userId);
		return true;
	}

}

package com.niton.jchad;

import com.niton.jauth.AccountManager;
import com.niton.jauth.AuthenticationHandler;
import com.niton.jchad.jpa.UserRepo;
import com.niton.jchad.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.naming.OperationNotSupportedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.niton.util.Logging.*;
import static com.niton.util.Logging.LogContext.*;

@Component
public class SessionHandler implements HandlerInterceptor, AuthenticationHandler<String,HttpServletRequest> {
	public final static String USER_ID = "uid",AUTHENTICATED = "authed";
	private AccountManager<String,HttpServletRequest> manager = new AccountManager<>(this);
	@Autowired
	private UserRepo repo;
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


	@Override
	public void sendInitPassword(String password, String user) {
		log(Level.WARNING, "Email sending is not possible right now");
	}

	@Override
	public void persistAuthenticateable(String user, byte[] hash) {
		log(new OperationNotSupportedException("Session handler does not supports Account creation"));
	}

	@Override
	public void deleteAuthenticateable(String id) {
		log(new OperationNotSupportedException("Session handler does not supports Account deletion"));
	}

	@Override
	public String getContextID(HttpServletRequest context) {
		return context.getRemoteAddr();
	}

	@Override
	public String getContextAuthInfo(HttpServletRequest context) {
		return context.getHeader("X-AUTH-SESSION");
	}

	@Override
	public String getAuthenticateable(String u) {
		return u;
	}

	@Override
	public void onSessionIpPermaBan(String ip) {
		log(SECURITY, String.format("IP %s is perma benned for session abuse", ip));
	}

	@Override
	public void onRapidSessionCheck(String ip, Integer integer) {
		log(SECURITY, String.format("Rapid session checking from %s(%d times)", ip, integer));
	}

	@Override
	public void sendResetTokenMail(String mail, String toString) {
		log(Level.WARNING, "Email sending is not possible right now");
	}

	@Override
	public boolean existsAuthenticatableById(String user) {
		return repo.existsById(user);
	}

	@Override
	public byte[] getHash(String user) {
		Optional<User> u = repo.findById(user);
		return u.map(User::getHash).orElse(null);
	}

	@Override
	public void setHash(String key, byte[] hash) {

	}
}

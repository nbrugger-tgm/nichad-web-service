package com.niton.jchad.security;

import com.niton.jauth.AuthenticationHandler;
import com.niton.jchad.jpa.UserRepo;
import com.niton.jchad.model.User;
import com.niton.util.Logging;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.naming.OperationNotSupportedException;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.niton.util.Logging.LogContext.SECURITY;
import static com.niton.util.Logging.log;

@Component
public class DatabaseAuthManager implements  AuthenticationHandler<User,HttpServletRequest> {

	private final UserRepo repo;

	@Autowired
	public DatabaseAuthManager(UserRepo repo) {
		this.repo = repo;
	}

	@Override
	public void sendInitPassword(String password, User user) {
		log(Logging.Level.WARNING, "Email sending is not possible right now");
	}

	@Override
	public void persistAuthenticateable(User user, byte[] hash) {
		user.setHash(hash);
		repo.save(user);
	}

	@Override
	public void deleteAuthenticateable(String id) {
		repo.deleteById(id);
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
	public User getAuthenticateable(String u) {
		return repo.findById(u).orElse(null);
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
		log(Logging.Level.WARNING, "Email sending is not possible right now");
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
		User u = repo.findById(key).orElse(null);
		if (u == null) {
			log(new NotFoundException("Cannot change password of non existent user"));
			return;
		}
		u.setHash(hash);
		repo.save(u);
	}
}

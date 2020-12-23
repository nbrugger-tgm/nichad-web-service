package com.niton.jchad.rest.implementation;

import com.niton.jauth.AccountManager;
import com.niton.jchad.ImageService;
import com.niton.jchad.jpa.InvitationRepo;
import com.niton.jchad.jpa.UserRepo;
import com.niton.jchad.model.Chat;
import com.niton.jchad.model.Invitation;
import com.niton.jchad.model.User;
import com.niton.jchad.rest.UserController;
import com.niton.jchad.rest.model.ChatResponse;
import com.niton.jchad.rest.model.LoginResponse;
import com.niton.jchad.rest.model.UserInformation;
import com.niton.jchad.security.DatabaseAuthManager;
import com.niton.login.LoginResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class UserControllerImpl implements UserController {

	private final UserRepo users;
	@Autowired
	private ImageService imageService;
	@Autowired
	private InvitationRepo invitations;
	private final AccountManager<User, HttpServletRequest> manager;

	@Autowired
	public UserControllerImpl(UserRepo repo) {
		this.users = repo;
		this.manager = new AccountManager<>(new DatabaseAuthManager(repo));
	}

	private static UserInformation toUserInformation(User m) {
		return new UserInformation(m.getDisplayName(), m.getProfilePictureId());
	}

	private static ChatResponse mapChat(Chat c) {
		return new ChatResponse()
				.withId(c.getID())
				.withName(c.getName())
				.withMembers(c.getMembers()
				              .stream()
				              .map(UserControllerImpl::toUserInformation)
				              .collect(Collectors.toSet()));
	}

	@Override
	public ResponseEntity<String> register(String id, @NotNull String displayName,String password) {
		if(users.existsById(id))
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		User u = new User();
		u.setId(id);
		u.setDisplayName(displayName);
		u.setProfilePictureId(null);
		manager.addAuthenticateable(u,password);
		return new ResponseEntity<>(manager.getID(u.getId()),HttpStatus.CREATED);
	}

	@Override
	public UserInformation getUser(String id, String me, @NotNull boolean authenticated) {
		if(accessible(id, me, authenticated)) {
			User u = users.getOne(id);
			return new UserInformation(u.getDisplayName(),u.getProfilePictureId());
		}
		throw new HttpClientErrorException(UNAUTHORIZED);

	}

	private boolean accessible(String id, String me, boolean authenticated) {
		return authenticated && (id.equals(me) || users.getOne(me)
		                                               .getChats()
		                                               .stream()
		                                               .anyMatch(c -> c.isMember(id)));
	}

	@Override
	public LoginResponse login(String id, String password,HttpServletRequest request) {
		LoginResult result = manager.authenticate(id,password,request.getRemoteAddr());
		LoginResponse response = new LoginResponse().withResult(result);

		if(result.success)
			response.setSessionID(manager.getID(id));

		return response;
	}

	@Override
	public String changeProfileImage(String id,
	                                 MultipartFile multipartFile,
	                                 String me,
	                                 @NotNull boolean authenticated) {
		if(isSelf(me, id, authenticated))
			throw new HttpClientErrorException(UNAUTHORIZED);
		User u = users.getOne(id);
		String oldId = u.getProfilePictureId();
		if(oldId != null) {
			try {
				imageService.removeImage(oldId);
			} catch (IOException e) {
				throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		String newId = RandomStringUtils.randomAlphanumeric(32);
		try {
			imageService.saveFile(newId,multipartFile);
		} catch (IOException e) {
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		u.setProfilePictureId(newId);
		users.save(u);

		return newId;
	}

	@Override
	public StreamingResponseBody getProfileImage(String id,
	                                             String me,
	                                             @NotNull boolean authenticated) {
		if(accessible(id,me,authenticated))
			return outputStream -> IOUtils.copy(imageService.getImageInputStream(users.getOne(id).getProfilePictureId()),outputStream);
		throw new HttpClientErrorException(UNAUTHORIZED);
	}

	@Override
	public void updateUserInfo(String id,
	                           @NotNull UserInformation update,
	                           String me,
	                           @NotNull boolean authenticated) {
		if (isSelf(id, me, authenticated))
			throw new HttpClientErrorException(UNAUTHORIZED);
		User u = users.getOne(id);
		if(update.getDisplayName() != null)
			u.setDisplayName(update.getDisplayName());
		users.save(u);
	}

	private boolean isSelf(String id, String me, @NotNull boolean authenticated) {
		return !authenticated || !me.equals(id);
	}

	@Override
	public Set<ChatResponse> findChats(String id, String me, @NotNull boolean authenticated) {
		if(!isSelf(id,me,authenticated))
			throw new HttpClientErrorException(UNAUTHORIZED);
		return users.getOne(id)
		            .getChats().stream()
		            .map(UserControllerImpl::mapChat)
		            .collect(Collectors.toSet());
	}

	@Override
	public Set<Invitation> getInvitations(String user, String me, @NotNull boolean authenticated) {
		if(!isSelf(user,me,authenticated))
			throw new HttpClientErrorException(UNAUTHORIZED);
		return invitations.findByUser(user);
	}
}

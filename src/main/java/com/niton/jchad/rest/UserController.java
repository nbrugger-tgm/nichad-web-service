package com.niton.jchad.rest;

import com.niton.jchad.SessionHandler;
import com.niton.jchad.model.Invitation;
import com.niton.jchad.rest.model.ChatResponse;
import com.niton.jchad.rest.model.LoginResponse;
import com.niton.jchad.rest.model.UserInformation;
import com.niton.jchad.verification.ValidId;
import com.niton.jchad.verification.ValidName;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.util.Set;

@RestController("/users")
public interface UserController {

	@PutMapping("/{id}")
	void register(
		@PathVariable
		@ValidId
		String id,
		@RequestParam
		@NotNull
		@ValidName
		String displayName
	);

	@GetMapping("/{id}")
	UserInformation getUser(
		@PathVariable
		@ValidId
		String id,
		@ValidId
		@Nullable
		@RequestAttribute(SessionHandler.USER_ID)
				String me,
		@NotNull
		@RequestAttribute(SessionHandler.AUTHENTICATED)
				boolean authenticated
	);

	@GetMapping("/{id}/session")
	LoginResponse login(
		@PathVariable
		@ValidId
		String id,
		@RequestParam
		String password
	);

	@PostMapping("/{id}/profile_image")
	String changeProfileImage(
			@PathVariable
			@ValidId
            String id,
			@RequestParam("image")
			MultipartFile multipartFile,
			@ValidId
			@Nullable
			@RequestAttribute(SessionHandler.USER_ID)
					String me,
			@NotNull
			@RequestAttribute(SessionHandler.AUTHENTICATED)
					boolean authenticated
	);

	@GetMapping("/{id}/profile_image")
	StreamingResponseBody getProfileImage(
			@PathVariable
			@ValidId
			String id,
			@ValidId
			@Nullable
			@RequestAttribute(SessionHandler.USER_ID)
					String me,
			@NotNull
			@RequestAttribute(SessionHandler.AUTHENTICATED)
					boolean authenticated
	);

	@PostMapping("/{id}")
	void updateUserInfo(
			@PathVariable
			@ValidId
			String id,
			@NotNull
			@RequestBody
			UserInformation update,
			@ValidId
			@Nullable
			@RequestAttribute(SessionHandler.USER_ID)
					String me,
			@NotNull
			@RequestAttribute(SessionHandler.AUTHENTICATED)
					boolean authenticated
	);

	@GetMapping("/{id}/chats")
	Set<ChatResponse> findChats(
			@PathVariable
					@ValidId
			String id,
			@ValidId
			@Nullable
			@RequestAttribute(SessionHandler.USER_ID)
					String me,
			@NotNull
			@RequestAttribute(SessionHandler.AUTHENTICATED)
					boolean authenticated
	);

	@GetMapping("/{user}/invitations")
	Set<Invitation> getInvitations(
			@PathVariable
					@ValidId
			String user,
			@ValidId
			@Nullable
			@RequestAttribute(SessionHandler.USER_ID)
					String me,
			@NotNull
			@RequestAttribute(SessionHandler.AUTHENTICATED)
					boolean authenticated
	);
}

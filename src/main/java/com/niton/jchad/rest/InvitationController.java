package com.niton.jchad.rest;

import com.niton.jchad.SessionHandler;
import com.niton.jchad.verification.ValidId;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController("/")
public interface InvitationController {
	@PutMapping("/chats/{chat}/invitations/{user}")
	HttpStatus invite(
			@PathVariable
					long chat,
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

	@DeleteMapping("/chats/{chat}/invitations/{user}")
	HttpStatus cancelInvitation(
			@PathVariable
					long chat,
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
	@DeleteMapping("/users/{user}/invitations/{chat}")
	HttpStatus discardInvitation(
			@ValidId
			@PathVariable
					String user,
			@PathVariable
					long chat,
			@ValidId
			@Nullable
			@RequestAttribute(SessionHandler.USER_ID)
					String me,
			@NotNull
			@RequestAttribute(SessionHandler.AUTHENTICATED)
					boolean authenticated
	);
	@PatchMapping("/users/{user}/invitations/{chat}")
	HttpStatus acceptInvitation(
			@ValidId
			@PathVariable
					String user,
			@PathVariable
					long chat,
			@ValidId
			@Nullable
			@RequestAttribute(SessionHandler.USER_ID)
					String me,
			@NotNull
			@RequestAttribute(SessionHandler.AUTHENTICATED)
					boolean authenticated
	);
	@DeleteMapping("/users/{user}/chats/{chat}")
	void leaveChat(
			@PathVariable
			@ValidId
			String user,
			@PathVariable
			long chat,
			@ValidId
			@Nullable
			@RequestAttribute(SessionHandler.USER_ID)
					String me,
			@NotNull
			@RequestAttribute(SessionHandler.AUTHENTICATED)
					boolean authenticated
	);
}

package com.niton.jchad.rest;

import com.niton.jchad.model.Chat;
import com.niton.jchad.rest.model.ChatResponse;
import com.niton.jchad.security.SessionHandler;
import com.niton.jchad.verification.ValidId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import static com.niton.jchad.NiChadApplication.USER_SESSION;

@RequestMapping
public interface InvitationController {
	@PutMapping("/chats/{chat}/invitations/{user}")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	void invite(
			@PathVariable
					long chat,
			@PathVariable
			@ValidId
					String user,
			@RequestBody
					String message,
			@ValidId
			@Nullable
			@RequestAttribute(SessionHandler.USER_ID)
					String me,
			@NotNull
			@RequestAttribute(SessionHandler.AUTHENTICATED)
					boolean authenticated
	);

	@DeleteMapping("/chats/{chat}/invitations/{user}")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	void cancelInvitation(
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
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	void discardInvitation(
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
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	ChatResponse acceptInvitation(
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
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
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

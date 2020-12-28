package com.niton.jchad.rest;

import com.niton.jchad.rest.model.ChatMetaData;
import com.niton.jchad.rest.model.MessageResponse;
import com.niton.jchad.security.SessionHandler;
import com.niton.jchad.verification.ValidId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;
import java.util.Set;

import static com.niton.jchad.NiChadApplication.USER_SESSION;

@RequestMapping("/chats")
public interface ChatController {
	@PutMapping
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	long createChat(@ValidId
	                @Nullable
	                @RequestAttribute(SessionHandler.USER_ID)
			                String me,
	                @NotNull
	                @RequestAttribute(SessionHandler.AUTHENTICATED)
			                boolean authenticated);

	@GetMapping("{chat}/meta")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	ChatMetaData getMetaData(
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


	@PostMapping("{chat}/messages/{sendingTime}")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	void sendMessage(
			@PathVariable
					long chat,
			@RequestBody
					byte[] msg,
			@Past
			@PathVariable
					LocalDateTime sendingTime,
			@ValidId
			@Nullable
			@RequestAttribute(SessionHandler.USER_ID)
					String me,
			@NotNull
			@RequestAttribute(SessionHandler.AUTHENTICATED)
					boolean authenticated
	);

	@GetMapping("{chat}/messages/unread")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	Set<MessageResponse> pollMessages(
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

	@GetMapping("{chat}/messages/")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	Set<MessageResponse> fetchMessages(
			@PathVariable
					long chat,
			@RequestParam
					LocalDateTime from,
			@RequestParam
					LocalDateTime to,
			@ValidId
			@Nullable
			@RequestAttribute(SessionHandler.USER_ID)
					String me,
			@NotNull
			@RequestAttribute(SessionHandler.AUTHENTICATED)
					boolean authenticated
	);

	@GetMapping("{chat}/messages/")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	Set<MessageResponse> fetchLastMessages(
			@PathVariable
					long chat,
			@RequestParam
					int count,
			@ValidId
			@Nullable
			@RequestAttribute(SessionHandler.USER_ID)
					String me,
			@NotNull
			@RequestAttribute(SessionHandler.AUTHENTICATED)
					boolean authenticated
	);


	@GetMapping("{chat}/messages/")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	Set<MessageResponse> fetchMessages(
			@PathVariable
					long chat,
			@RequestParam
					int from,
			@RequestParam
					int count,
			@ValidId
			@Nullable
			@RequestAttribute(SessionHandler.USER_ID)
					String me,
			@NotNull
			@RequestAttribute(SessionHandler.AUTHENTICATED)
					boolean authenticated
	);

	@GetMapping("{chat}/messages/last")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	MessageResponse fetchLastMessage(
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

	@PatchMapping("{chat}/messages/{sender}")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	void markAsRead(
			@PathVariable
					long chat,
			@RequestParam
					LocalDateTime msgSendingTime,
			@PathVariable("sender")
					String senderId,
			@ValidId
			@Nullable
			@RequestAttribute(SessionHandler.USER_ID)
					String me,
			@NotNull
			@RequestAttribute(SessionHandler.AUTHENTICATED)
					boolean authenticated
	);

	@PatchMapping("{chat}/member/{member}/permissions")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	void promote(
			@PathVariable
					long chat,
			@PathVariable
					String member,
			@ValidId
			@Nullable
			@RequestAttribute(SessionHandler.USER_ID)
					String me,
			@NotNull
			@RequestAttribute(SessionHandler.AUTHENTICATED)
					boolean authenticated
	);

}

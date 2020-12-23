package com.niton.jchad.rest;

import com.niton.jchad.SessionHandler;
import com.niton.jchad.rest.model.ChatMetaData;
import com.niton.jchad.rest.model.MessageResponse;
import com.niton.jchad.verification.ValidId;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;
import java.util.Set;

@RestController("/chats/")
public interface ChatController {
	@PutMapping
	long createChat();

	@GetMapping("{chat}/meta")
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


	@PostMapping("{chat}/messages")
	void sendMessage(
		@PathVariable
		long chat,
		@RequestBody
		byte[] msg,
		@Past
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
}

package com.niton.jchad.rest;

import com.niton.jchad.rest.model.ChatMetaData;
import com.niton.jchad.rest.model.MessageResponse;
import org.springframework.web.bind.annotation.*;

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
			long chat
	);


	@PostMapping("{chat}/messages")
	void sendMessage(
		@PathVariable
		long chat,
		@RequestBody
		byte[] msg,
		@Past
		LocalDateTime sendingTime
	);

	@GetMapping("{chat}/messages/unread")
	Set<MessageResponse> pollMessages(
		@PathVariable
		long chat
	);

	@GetMapping("{chat}/messages/")
	Set<MessageResponse> fetchMessages(
		@PathVariable
		long chat,
		@RequestParam
		LocalDateTime from,
		@RequestParam
		LocalDateTime to
	);
	@GetMapping("{chat}/messages/")
	Set<MessageResponse> fetchLastMessages(
		@PathVariable
		long chat,
		@RequestParam
		int count
	);


	@GetMapping("{chat}/messages/")
	Set<MessageResponse> fetchMessages(
		@PathVariable
		long chat,
		@RequestParam
		int from,
		@RequestParam
		int count
	);
	@GetMapping("{chat}/messages/last")
	MessageResponse fetchLastMessage(
		@PathVariable
		long chat
	);

	@PatchMapping("{chat}/messages/{sender}")
	void markAsRead(
			@PathVariable
			long chat,
			@RequestParam
			LocalDateTime msgSendingTime,
			@PathVariable("sender")
			String senderId
	);
}

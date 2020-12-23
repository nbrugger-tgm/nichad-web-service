package com.niton.jchad.rest;

import com.niton.jchad.verification.ValidId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController("/")
public interface InvitationController {
	@PutMapping("/chats/{chat}/invitations/{user}")
	HttpStatus invite(
			@PathVariable
					long chat,
			@PathVariable
			@ValidId
					String user
	);

	@DeleteMapping("/chats/{chat}/invitations/{user}")
	HttpStatus cancelInvitation(
			@PathVariable
					long chat,
			@PathVariable
			@ValidId
					String user
	);
	@DeleteMapping("/users/{user}/invitations/{chat}")
	HttpStatus discardInvitation(
			@ValidId
			@PathVariable
					String user,
			@PathVariable
					long chat
	);
	@PatchMapping("/users/{user}/invitations/{chat}")
	HttpStatus acceptInvitation(
			@ValidId
			@PathVariable
					String user,
			@PathVariable
					long chat
	);
	@DeleteMapping("/users/{user}/chats/{chat}")
	void leaveChat(
			@PathVariable
			@ValidId
			String user,
			@PathVariable
			long chat
	);
}

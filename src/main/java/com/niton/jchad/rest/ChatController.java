package com.niton.jchad.rest;

import com.niton.jchad.rest.model.ChatMetaData;
import com.niton.jchad.rest.model.ChatResponse;
import com.niton.jchad.rest.model.MessageResponse;
import com.niton.jchad.security.SessionHandler;
import com.niton.jchad.verification.ValidId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.niton.jchad.NiChadApplication.USER_SESSION;

@RequestMapping("/chats")
public interface ChatController extends Endpoint {
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

	@GetMapping("{chat}")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	ChatResponse getChat(
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
	@PatchMapping("{chat}")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	void editChat(
			@PathVariable
					long chat,
			@RequestBody
				ChatEditRequest editRequest,
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
	@PutMapping("{chat}/messages/{messageTime}/{sender}/answer/{answerTime}")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	void answer(
			@PathVariable
					long chat,
			@RequestBody
					byte[] msg,
			@Past
			@PathVariable
					LocalDateTime messageTime,
			@PathVariable("sender")
					String originalSender,
			@Past
			@PathVariable
					LocalDateTime answerTime,
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
	List<MessageResponse> pollMessages(
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
	List<MessageResponse> fetchMessageByTime(
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

	@GetMapping("{chat}/messages/last")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	List<MessageResponse> fetchLastMessages(
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


	@GetMapping("{chat}/messages/segment")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	List<MessageResponse> fetchMessageSegment(
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

	@GetMapping("{chat}/last")
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

	@GetMapping("{chat}/image")
	StreamingResponseBody getChatImage(
			@PathVariable long chat,
			@ValidId
			@Nullable
			@RequestAttribute(SessionHandler.USER_ID)
					String me,
			@NotNull
			@RequestAttribute(SessionHandler.AUTHENTICATED)
					boolean authenticated
	);
	@PostMapping("{chat}/image")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	String updateChatImage(
			@PathVariable
			@ValidId
					long chat,
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
}

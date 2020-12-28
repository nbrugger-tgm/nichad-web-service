package com.niton.jchad.rest;

import com.niton.jchad.model.Invitation;
import com.niton.jchad.rest.model.ChatResponse;
import com.niton.jchad.rest.model.InvitationResponse;
import com.niton.jchad.rest.model.LoginResponse;
import com.niton.jchad.rest.model.UserInformation;
import com.niton.jchad.security.SessionHandler;
import com.niton.jchad.verification.ValidId;
import com.niton.jchad.verification.ValidName;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

import static com.niton.jchad.NiChadApplication.USER_SESSION;

@RequestMapping("users")
public interface UserController {

	@PutMapping("{id}")
	HttpEntity<String> register(
			@PathVariable
			@ValidId
					String id,
			@RequestParam
			@NotNull
			@ValidName
					String displayName,
			@RequestParam
			@Size(min = 8, max = 128)
					String password
	);

	@GetMapping("{id}")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
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

	@GetMapping("{id}/session")
	LoginResponse login(
			@PathVariable
			@ValidId
					String id,
			@RequestParam
					String password,
			HttpServletRequest request
	);

	@PostMapping("{id}/profile_image")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
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

	@GetMapping("{id}/profile_image")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
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

	@PostMapping("{id}")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
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

	@GetMapping("{id}/chats")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
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

	@GetMapping("{user}/invitations")
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	Set<InvitationResponse> getInvitations(
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

	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	@GetMapping("{user}/authentication")
	boolean testAuthenticationStatus(
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
	@Operation(security = {@SecurityRequirement(name = USER_SESSION)})
	@DeleteMapping("{user}/authentication")
	void removeAuthentication(
			@PathVariable
			@ValidId
					String user,
			@ValidId
			@Nullable
			@RequestAttribute(SessionHandler.USER_ID)
					String me,
			@RequestAttribute(SessionHandler.AUTHENTICATED)
					@NotNull boolean authenticated
	);
}

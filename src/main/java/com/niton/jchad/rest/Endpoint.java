package com.niton.jchad.rest;

import com.niton.jchad.jpa.ChatRepo;
import com.niton.jchad.jpa.InvitationRepo;
import com.niton.jchad.jpa.UserRepo;
import com.niton.jchad.model.Invitation;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.constraints.NotNull;

import static org.springframework.http.HttpStatus.*;

public interface Endpoint {
	UserRepo getUserRepo();
	ChatRepo getChatRepo();
	InvitationRepo getInvitationRepo();
	default void checkUserAndChatExistence(long chat, String user) {
		if(getUserRepo().existsById(user))
			throw new HttpClientErrorException(NOT_FOUND, "USER NOT FOUND");
		if(getChatRepo().existsById(chat))
			throw new HttpClientErrorException(NOT_FOUND,"CHAT NOT FOUND");
	}

	default void checkAdminPermission(long chat, String me) {
		if (!getChatRepo().getOne(chat).getMember(me).isAdmin())
			throw new HttpClientErrorException(UNAUTHORIZED, "INSUFFICIENT PERMISSIONS");
	}

	default void checkAuthenticated(@NotNull boolean authenticated) {
		if (!authenticated)
			throw new HttpClientErrorException(UNAUTHORIZED);
	}
	default void checkAuthority(String user, String me) {
		if(!user.equals(me))
			throw new HttpClientErrorException(UNAUTHORIZED, "INSUFFICIENT PERMISSIONS");
	}

	default void checkMembership(String user, long chat) {
		if(getChatRepo().getOne(chat).getMember(user) == null)
			throw new HttpClientErrorException(BAD_REQUEST,"NOT A MEMBER");
	}

	default void checkUserExistence(String me) {
		if(!getUserRepo().existsById(me))
			throw new HttpClientErrorException(NOT_FOUND,"USER NOT FOUND");
	}
}

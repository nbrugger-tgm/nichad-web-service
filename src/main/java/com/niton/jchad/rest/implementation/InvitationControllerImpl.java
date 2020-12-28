package com.niton.jchad.rest.implementation;

import com.niton.jchad.jpa.ChatRepo;
import com.niton.jchad.jpa.InvitationRepo;
import com.niton.jchad.jpa.UserRepo;
import com.niton.jchad.model.Chat;
import com.niton.jchad.model.Invitation;
import com.niton.jchad.model.InvitationId;
import com.niton.jchad.model.Member;
import com.niton.jchad.rest.InvitationController;
import com.niton.jchad.rest.model.ChatResponse;
import com.niton.jchad.rest.model.InvitationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.constraints.NotNull;

import static org.springframework.http.HttpStatus.*;

@RestController
public class InvitationControllerImpl implements InvitationController {
	@Autowired
	private UserRepo users;
	@Autowired
	private InvitationRepo invitations;
	@Autowired
	private ChatRepo chats;

	@Override
	public InvitationResponse invite(long chat,
	                                 String user,
	                                 String message,
	                                 String me,
	                                 @NotNull boolean authenticated) {
		checkAuthenticated(authenticated);
		checkUserAndChatExistence(chat, user);
		checkAdminPermission(chat, me);
		if(!invitations.existsById_InvitedAndId_Chat(user, chat)){
			Invitation inv = new Invitation(users.getOne(me),new InvitationId(users.getOne(user),chats.getOne(chat)),message);
			invitations.save(inv);
			return inv.response();
		}else
			throw new HttpClientErrorException(CONFLICT,"USER ALREADY INVITED");
	}

	@Override
	public UserRepo getUserRepo() {
		return users;
	}

	@Override
	public ChatRepo getChatRepo() {
		return chats;
	}

	@Override
	public InvitationRepo getInvitationRepo() {
		return invitations;
	}

	@Override
	public void cancelInvitation(long chat,
	                                   String user,
	                                   String me,
	                                   @NotNull boolean authenticated) {
		checkAuthenticated(authenticated);
		checkInvitationExistence(user, chat);
		checkAdminPermission(chat, me);
		invitations.deleteById(new InvitationId(users.getOne(user),chats.getOne(chat)));
	}



	@Override
	public void discardInvitation(String user,
	                                    long chat,
	                                    String me,
	                                    @NotNull boolean authenticated) {
		checkAuthenticated(authenticated);
		checkInvitationExistence(user, chat);
		checkAuthority(user, me);
		invitations.deleteById(new InvitationId(users.getOne(user),chats.getOne(chat)));
	}

	private void checkInvitationExistence(String user, long chat) {
		if(!invitations.existsById_InvitedAndId_Chat(user, chat))
			throw new HttpClientErrorException(NOT_FOUND,"INVITATION NOT FOUND");
	}

	@Override
	public ChatResponse acceptInvitation(String user,
	                                     long chat,
	                                     String me,
	                                     @NotNull boolean authenticated) {
		checkAuthenticated(authenticated);
		checkAuthority(user,me);
		checkInvitationExistence(user,chat);
		invitations.deleteById_InvitedAndId_Chat(user, chat);
		Chat c = chats.getOne(chat);
		c.getMembers().add(new Member().withUser(users.getOne(user)));
		chats.save(c);
		return c.createResponeData(me);
	}

	@Override
	public void leaveChat(String user, long chat, String me, @NotNull boolean authenticated) {
		checkAuthenticated(authenticated);
		checkAuthority(me,user);
		checkUserAndChatExistence(chat,user);
		checkMembership(user,chat);
		Chat c = chats.getOne(chat);
		Member m = c.getMember(me);
		c.getMembers().remove(m);
		chats.save(c);
	}
}

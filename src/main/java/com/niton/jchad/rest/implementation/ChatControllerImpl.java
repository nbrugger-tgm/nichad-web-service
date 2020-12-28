package com.niton.jchad.rest.implementation;

import com.niton.jchad.ImageService;
import com.niton.jchad.jpa.ChatRepo;
import com.niton.jchad.jpa.InvitationRepo;
import com.niton.jchad.jpa.MessageRepo;
import com.niton.jchad.jpa.UserRepo;
import com.niton.jchad.model.*;
import com.niton.jchad.rest.ChatController;
import com.niton.jchad.rest.ChatEditRequest;
import com.niton.jchad.rest.model.ChatMetaData;
import com.niton.jchad.rest.model.ChatResponse;
import com.niton.jchad.rest.model.MessageResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
public class ChatControllerImpl implements ChatController {
	@Autowired
	private UserRepo users;
	@Autowired
	private InvitationRepo invitations;
	@Autowired
	private ChatRepo chats;

	@Autowired
	private MessageRepo messages;
	@Autowired
	private ImageService imageService;

	@Override
	public long createChat(String me, @NotNull boolean authenticated) {
		checkAuthenticated(authenticated);
		checkUserExistence(me);
		Chat chat = new Chat();
		chat.addMember(users.getOne(me),true);
		chats.save(chat);
		return chat.getID();
	}


	@Override
	public ChatMetaData getMetaData(long chat, String me, @NotNull boolean authenticated) {
		checkAuthenticated(authenticated);
		checkMembership(me,chat);
		return chats.getOne(chat).getMetaData();
	}

	@Override
	public ChatResponse getChat(long chat, String me, @NotNull boolean authenticated) {
		checkAuthenticated(authenticated);
		checkMembership(me,chat);
		return chats.getOne(chat).createResponeData(me);
	}

	@Override
	public void editChat(long chat,
	                     ChatEditRequest editRequest,
	                     String me,
	                     @NotNull boolean authenticated) {

		checkAuthenticated(authenticated);
		checkMembership(me,chat);
		checkAdminPermission(chat,me);
		Chat c = chats.getOne(chat);
		c.setName(editRequest.getName().orElse(c.getName()));
		chats.save(c);
	}

	@Override
	public void sendMessage(long chat,
	                        byte[] msg,
	                        @Past LocalDateTime sendingTime,
	                        String me,
	                        @NotNull boolean authenticated) {
		checkAuthenticated(authenticated);
		checkMembership(me,chat);
		Chat c = chats.getOne(chat);
		c.sendMessage(users.getOne(me),msg,sendingTime);
		chats.save(c);
	}

	@Override
	public void answer(long chat,
	                   byte[] msg,
	                   @Past LocalDateTime messageTime,
	                   String originalSender,
	                   @Past LocalDateTime answerTime,
	                   String me,
	                   @NotNull boolean authenticated) {
		checkAuthenticated(authenticated);
		checkMembership(me,chat);
		Chat c = chats.getOne(chat);
		c.answerMessage(messageTime, users.getOne(originalSender), answerTime,users.getOne(me),msg);
		chats.save(c);
	}

	@Override
	public List<MessageResponse> pollMessages(long chat, String me, @NotNull boolean authenticated) {
		return getMessages(
					chat,
					me,
					authenticated,
					()->this.messages.findByChatAndReceiveTimesIsNotContainingOrderBySendingTime(
							chat, users.getOne(me)
					)
		);
	}

	private List<MessageResponse> getMessages(long chat, String me, boolean authenticated, Supplier<List<Message>> getter) {
		checkAuthenticated(authenticated);
		checkMembership(me, chat);
		List<Message> messages = getter.get();
		messages.forEach(m -> {if(!m.containsKey(users.getOne(me))) m.put(users.getOne(me),LocalDateTime.now());});
		this.messages.saveAll(messages);
		return messages.stream()
		               .map(Message::response)
		               .collect(Collectors.toList());
	}

	@Override
	public List<MessageResponse> fetchMessageByTime(long chat,
	                                               LocalDateTime from,
	                                               LocalDateTime to,
	                                               String me,
	                                               @NotNull boolean authenticated) {
		return getMessages(
				chat,
				me,
				authenticated,
				()->this.messages.findByChatAndSendingTimeBetweenOrderBySendingTime(
						chat,from,to
				)
		);
	}

	@Override
	public List<MessageResponse> fetchLastMessages(long chat,
	                                               int count,
	                                               String me,
	                                               @NotNull boolean authenticated) {

		return getMessages(
				chat,
				me,
				authenticated,
				()->this.messages.findByChatOrderBySendingTime(
						chat, PageRequest.of(0,count)
				)
		);
	}

	/**
	 * @param chat
	 * @param from needs to be a multiple of count
	 * @param count
	 * @param me
	 * @param authenticated
	 * @return
	 */
	@Override
	public List<MessageResponse> fetchMessageSegment(long chat,
	                                                 int from,
	                                                 int count,
	                                                 String me,
	                                                 @NotNull boolean authenticated) {
		return getMessages(
				chat,
				me,
				authenticated,
				()->this.messages.findByChatOrderBySendingTime(
						chat, PageRequest.of(from/count,count)
				)
		);
	}

	@Override
	public MessageResponse fetchLastMessage(long chat, String me, @NotNull boolean authenticated) {
		checkAuthenticated(authenticated);
		checkMembership(me,chat);
		Message msg = messages.findMessageByChatOrderBySendingTime(chat);
		if(msg == null)
			throw new HttpClientErrorException(NOT_FOUND, "NO MESSAGE FOUND");
		if(msg.containsKey(users.getOne(me))) {
			msg.put(users.getOne(me), LocalDateTime.now());
			messages.save(msg);
		}
		return msg.response();
	}

	@Override
	public void markAsRead(long chat,
	                       @PastOrPresent
	                       LocalDateTime msgSendingTime,
	                       String senderId,
	                       String me,
	                       @NotNull boolean authenticated) {
		checkAuthenticated(authenticated);
		checkMembership(me,chat);
		if(!messages.existsBySendingTimeAndSenderAndChat(msgSendingTime,senderId,chat))
			throw new HttpClientErrorException(NOT_FOUND, "MESSAGE NOT FOUND");
		Chat c = chats.getOne(chat);
		Member m = c.getMember(me);
		if(m.getLastReadDate().isAfter(msgSendingTime) || m.getLastReadDate().isEqual(msgSendingTime))
			throw new HttpClientErrorException(BAD_REQUEST,"THIS MESSAGE IS ALREADY READ");
		c.getMember(me).setLastReadDate(msgSendingTime);
		chats.save(c);
	}

	@Override
	public void promote(long chat, String member, String me, @NotNull boolean authenticated) {
		checkAuthenticated(authenticated);
		checkMembership(me,chat);
		checkMembership(member,chat);
		checkAdminPermission(chat,me);
		Chat c = chats.getOne(chat);
		c.getMember(member).setAdmin(true);
		chats.save(c);
	}

	@Override
	public StreamingResponseBody getChatImage(long chat,
	                                          String me,
	                                          @NotNull boolean authenticated) {
		checkAuthenticated(authenticated);
		checkMembership(me,chat);
		return chats.getOne(chat).getImage(imageService, me);
	}

	@Override
	public String updateChatImage(long chat,
	                              MultipartFile multipartFile,
	                              String me,
	                              @NotNull boolean authenticated) {
		checkAuthenticated(authenticated);
		checkMembership(me,chat);
		checkAdminPermission(chat,me);
		Chat c = chats.getOne(chat);
		String newId;
		do{
			newId = RandomStringUtils.randomAlphanumeric(32);
		}while (imageService.hasImage(newId));
		String oldId = c.getPictureId();
		try {
			imageService.saveFile(newId,multipartFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new HttpServerErrorException(INTERNAL_SERVER_ERROR);
		}
		if(oldId != null) {
			try {
				imageService.removeImage(oldId);
			} catch (IOException e) {
				e.printStackTrace();
				throw new HttpServerErrorException(INTERNAL_SERVER_ERROR);
			}
		}
		return newId;
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
}

package com.niton.jchad.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class Message {
	@Id
	@ManyToOne(optional = false)
	private Chat chat;

	@Id
	@ManyToOne(optional = false)
	private User sender;

	@ManyToOne(optional = true)
	private Message referenceMessage;

	private LocalDateTime receiveTime;

	@Id
	private LocalDateTime sendingTime;

	@ManyToMany
	private Set<User> readBy;

	public boolean isReadByAll(){
		return readBy.containsAll(chat.getMembers());
	}

	/**
	 * Encrypted text
	 */
	private byte[]    text;
}

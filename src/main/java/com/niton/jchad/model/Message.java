package com.niton.jchad.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@IdClass(MessageId.class)
public class Message implements Serializable {
	@Id
	@ManyToOne(optional = false)
	private Chat chat;

	@Id
	private LocalDateTime sendingTime;

	@Id
	@ManyToOne(optional = false)
	private User sender;

	@ManyToOne(optional = true)
	private Message referenceMessage;

	private LocalDateTime receiveTime;


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

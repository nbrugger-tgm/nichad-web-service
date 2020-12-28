package com.niton.jchad.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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
	@Column(name="sending_time")
	private LocalDateTime sendingTime;

	@Id
	@ManyToOne(optional = false)
	private User sender;

	@ManyToOne
	private Message referenceMessage;

	@ElementCollection
	private Map<User,LocalDateTime> receiveTimes;

	/**
	 * Encrypted text
	 */
	private byte[] text;

	public boolean isReadByAll() {
		return chat.getMembers().stream()
		           .allMatch(
			           e ->
				           e.getLastReadDate().isAfter(receiveTimes.get(e.getUser())) ||
				           e.getLastReadDate().isEqual(receiveTimes.get(e.getUser())) ||
				           e.getUser().equals(sender)
		           );


	}
}

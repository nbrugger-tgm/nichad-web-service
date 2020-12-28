package com.niton.jchad.model;

import com.niton.jchad.rest.model.MessageResponse;
import lombok.*;
import lombok.experimental.Delegate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@With
@AllArgsConstructor
@NoArgsConstructor
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
	@Nullable
	private Message referenceMessage;

	@ElementCollection
	@Delegate
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
	public boolean isReadBy(String user) {
		Member m = chat.getMember(user);
		return
				m.getLastReadDate().isAfter(getReceiveTimes().get(m.getUser())) ||
				m.getLastReadDate().isEqual(getReceiveTimes().get(m.getUser())) ||
				getSender().getId().equals(user);
	}

	public MessageResponse response() {
		return new MessageResponse()
				.withSendingTime(getSendingTime())
				.withReceivingTimes(
					getReceiveTimes()
						.entrySet()
						.stream()
						.collect(
							Collectors.toMap(
                                e -> e.getKey().getId(),
                                Map.Entry::getValue
                            )
						)
				)
				.withSenderId(getSender().getId())
				.withText(getText())
				.withReferenceMessage(referenceMessage != null ? referenceMessage.response() : null);
	}
}

package com.niton.jchad.model;

import com.niton.jchad.rest.implementation.UserControllerImpl;
import com.niton.jchad.rest.model.ChatResponse;
import com.niton.jchad.verification.ValidName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class Chat implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long ID;

	@ValidName
	private String name;

	@OneToMany(mappedBy = "chat")
	private Set<Member> members;

	@OneToMany(mappedBy = "chat")
	private Set<Message> messages;

	@Size(min = 1024)
	private byte[] encryptionKey;

	@Nullable
	private String pictureId;

	//@ElementCollection
	//private Map<User, LocalDateTime> readStatusSet;


	public boolean isMember(String id) {
		return members.stream().map(i -> i.getUser().getId()).anyMatch(id::equals);
	}

	public Member getMember(String me) {
		return getMembers().stream().filter(m -> m.getUser().getId().equals(me)).findFirst().orElse(null);
	}

	public ChatResponse createResponeData() {
		return new ChatResponse()
				.withId(getID())
				.withName(getName())
				.withMembers(getMembers()
				              .stream()
				              .map(Member::getUser)
				              .map(User::getUserInformation)
				              .collect(Collectors.toSet()));
	}
}

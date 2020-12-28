package com.niton.jchad.model;

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
}

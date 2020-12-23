package com.niton.jchad.model;

import com.niton.jchad.verification.ValidName;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomUtils;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
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

	@ManyToMany
	@JoinTable(name="member")
	private Set<User> members;

	@OneToMany(mappedBy = "chat")
	private Set<Message> messages;

	@Size(min=1024)
	private byte[] encryptionKey;


	public boolean isMember(String id){
		return members.stream().map(User::getId).anyMatch(id::equals);
	}
}

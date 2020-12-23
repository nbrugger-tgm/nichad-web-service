package com.niton.jchad.model;

import com.niton.jchad.verification.ValidId;
import com.niton.jchad.verification.ValidName;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Setter
@Getter
public class User {
	@Id
	@ValidId
	private String    id;

	@ValidName
	private String    displayName;

	@ManyToMany(mappedBy = "members")
	@JoinTable(name="member")
	private Set<Chat> chats;

	private String    profilePictureId;

	//Todo: @NotNull Can be removed as soon as password sending per mail is enabled
	@NotNull
	private byte[] hash;

}

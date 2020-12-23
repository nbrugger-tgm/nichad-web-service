package com.niton.jchad.model;

import com.niton.jchad.verification.ValidId;
import com.niton.jchad.verification.ValidName;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class User implements Serializable {
	@Id
	@ValidId
	@NonNull
	private String    id;

	@ValidName
	@NotNull
	private String    displayName;

	@ManyToMany(mappedBy = "members")
	private Set<Chat> chats;

	private String    profilePictureId;

	//Todo: @NotNull Can be removed as soon as password sending per mail is enabled
	@NotNull
	@NonNull
	private byte[] hash;

}

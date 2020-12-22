package com.niton.jchad.model;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
	@Id
	@NotNull
	@NonNull
	private String id;
	private String displayName;
	@OneToMany
	private List<Chat> contacts;
	@Nullable
	private String profilePictureId;
	@ManyToMany
	private List<User> pendingAddingRequests;
	@NotNull
	@NonNull
	private byte[] hash;

}

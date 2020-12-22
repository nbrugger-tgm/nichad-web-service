package com.niton.jchad.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Setter
@Getter
public class User {
	@Id
	private String id;
	private String displayName;
	@OneToMany
	private List<Chat> contacts;
	private String profilePictureId;

}

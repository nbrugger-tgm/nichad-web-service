package com.niton.jchad.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
@Getter
@Setter
public class Chat {
	@Id
	@OneToOne
	private User u1;

	@Id
	@OneToOne
	private User u2;

	@OneToMany
	private List<Message> messages;
}

package com.niton.jchad.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
public class Invitation {
	@ManyToOne(optional = false)
	private User from;

	@Id
	@ManyToOne(optional = false)
	private User invited;

	@Id
	@ManyToOne(optional = false)
	private Chat chat;
	
	@Size(max=500)
	@Nullable
	private String message;
}

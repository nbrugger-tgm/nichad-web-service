package com.niton.jchad.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Entity
public class Invitation implements Serializable {
	@ManyToOne(optional = false)
	private User from;

	@EmbeddedId
	private InvitationId id;
	
	@Size(max=500)
	@Nullable
	private String message;
}

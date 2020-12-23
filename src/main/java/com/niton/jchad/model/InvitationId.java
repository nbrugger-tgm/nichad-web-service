package com.niton.jchad.model;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class InvitationId implements Serializable {
	@ManyToOne(optional = false)
	public User invited;
	@ManyToOne(optional = false)
	public Chat chat;
}

package com.niton.jchad.model;

import com.niton.jchad.rest.model.InvitationResponse;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@With
public class Invitation implements Serializable {
	@ManyToOne(optional = false)
	private User from;

	@EmbeddedId
	private InvitationId id;

	@Size(max = 500)
	@Nullable
	private String message;

	public InvitationResponse response() {
		return new InvitationResponse(from.getId(),id.getInvited().getId(),getMessage(),getId().getChat().getID());
	}
}

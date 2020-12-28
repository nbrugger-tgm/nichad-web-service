package com.niton.jchad.rest.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.niton.jchad.verification.ValidId;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class InvitationResponse {
	@ValidId
	String from;
	@ValidId
	String to;
	@Nullable
	@Size(max=500)
	String msg;
	@NotNull
	long chat;
}

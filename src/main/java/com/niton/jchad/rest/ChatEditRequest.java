package com.niton.jchad.rest;

import com.niton.jchad.verification.ValidName;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class ChatEditRequest {
	@ValidName
	private Optional<String> name;
}

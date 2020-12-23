package com.niton.jchad.rest.model;

import com.niton.jchad.verification.ValidName;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@With
@NoArgsConstructor
public class ChatResponse {
	private Set<UserInformation> members;
	@Nullable
	@ValidName
	private String               name;
	@NotNull
	private long                 id;
}

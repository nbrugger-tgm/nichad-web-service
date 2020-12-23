package com.niton.jchad.rest.model;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@With
public class MessageResponse {
	@NotNull
	@NonNull
	private byte[] text;
	@NotNull
	@NonNull
	private String senderId;
	@Nullable
	private MessageResponse referenceMessage;
	@NotNull
	@NonNull
	private LocalDateTime sendingTime;
}

package com.niton.jchad.rest.model;

import lombok.*;
import lombok.experimental.Delegate;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@With
public class MessageResponse {
	@NotNull
	@NonNull
	private byte[]          text;
	@NotNull
	@NonNull
	private String          senderId;
	@Nullable
	private MessageResponse referenceMessage = null;
	@NotNull
	@NonNull
	private LocalDateTime   sendingTime;

	@NotNull
	@Delegate
	private Map<String,LocalDateTime> receivingTimes = new HashMap<>();
}

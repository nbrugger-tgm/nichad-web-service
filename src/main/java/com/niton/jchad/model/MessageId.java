package com.niton.jchad.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
@EqualsAndHashCode
public class MessageId implements Serializable {
	private User      sender;
	private LocalTime sendingTime;
	private Chat      chat;
}

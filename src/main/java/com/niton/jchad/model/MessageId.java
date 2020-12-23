package com.niton.jchad.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageId implements Serializable {
	private User sender;
	private LocalTime sendingTime;
	private Chat chat;
}

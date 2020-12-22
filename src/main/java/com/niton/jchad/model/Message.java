package com.niton.jchad.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalTime;

@Getter
@Setter
@Entity
public class Message {
	@Id
	@ManyToOne
	private Chat chat;
	@Id
	private LocalTime time;
	private String text;
}

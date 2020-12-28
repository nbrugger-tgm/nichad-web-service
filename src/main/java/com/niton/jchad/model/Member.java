package com.niton.jchad.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
@Entity
@EqualsAndHashCode
@IdClass(Member.Key.class)
public class Member {
	@NotNull
	private LocalDateTime lastReadDate = LocalDateTime.now();

	@ManyToOne
	@Id
	private User user;
	@ManyToOne
	@Id
	private Chat chat;

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@With
	@EqualsAndHashCode
	public static class Key implements Serializable {
		private User user;
		private Chat chat;
	}
}
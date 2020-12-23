package com.niton.jchad.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@NoArgsConstructor
@With
@AllArgsConstructor
public class ChatMetaData {
	private int  members;
	private long messageCount;
}

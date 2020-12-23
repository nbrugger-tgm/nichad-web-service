package com.niton.jchad.rest.model;

import com.niton.jchad.verification.ValidName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInformation {
	@ValidName
	private String displayName;
	private String imageId;
}

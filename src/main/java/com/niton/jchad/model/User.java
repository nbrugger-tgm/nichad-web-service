package com.niton.jchad.model;

import com.niton.jchad.rest.model.UserInformation;
import com.niton.jchad.verification.ValidId;
import com.niton.jchad.verification.ValidName;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class User implements Serializable {
	@Id
	@ValidId
	@NonNull
	private String id;

	@ValidName
	@NotNull
	private String displayName;

	@OneToMany(mappedBy = "user")
	private Set<Member> memberships;

	private String profilePictureId;

	//Todo: @NotNull Can be removed as soon as password sending per mail is enabled
	@NotNull
	@NonNull
	private byte[] hash;

	@NotNull
	private boolean deleted = false;

	@Override
	public String toString() {
		return String.format("%s(%s)", getDisplayName(), getId());
	}

	public UserInformation getUserInformation() {
		return new UserInformation(getDisplayName(), getProfilePictureId());
	}
}

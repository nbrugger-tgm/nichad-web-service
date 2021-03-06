package com.niton.jchad.jpa;

import com.niton.jchad.model.Invitation;
import com.niton.jchad.model.InvitationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface InvitationRepo extends JpaRepository<Invitation, InvitationId> {
	@Query("select inv from Invitation inv WHERE inv.id.invited.id = ?1")
	Set<Invitation> findByInvited(String u);
	boolean existsById_InvitedAndId_Chat(String user, long chat);

	void deleteById_InvitedAndId_Chat(String user, long chat);
}

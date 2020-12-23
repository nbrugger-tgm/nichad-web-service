package com.niton.jchad.jpa;

import com.niton.jchad.model.Invitation;
import com.niton.jchad.model.InvitationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepo extends JpaRepository<Invitation, InvitationId> { }

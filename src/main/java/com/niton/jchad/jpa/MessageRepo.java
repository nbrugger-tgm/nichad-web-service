package com.niton.jchad.jpa;

import com.niton.jchad.model.Message;
import com.niton.jchad.model.MessageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepo extends JpaRepository<Message, MessageId> {
}

package com.niton.jchad.jpa;

import com.niton.jchad.model.Chat;
import com.niton.jchad.model.ChatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepo extends JpaRepository<Chat, ChatId> {
}

package com.niton.jchad;

import com.niton.jchad.jpa.ChatRepo;
import com.niton.jchad.jpa.MessageRepo;
import com.niton.jchad.jpa.UserRepo;
import com.niton.jchad.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
	@Autowired
	private UserRepo    userRepo;
	@Autowired
	private MessageRepo messageRepo;
	@Autowired
	private ChatRepo    chatRepo;
}

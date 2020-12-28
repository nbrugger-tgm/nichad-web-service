package com.niton.jchad.jpa;

import com.niton.jchad.model.Message;
import com.niton.jchad.model.MessageId;
import com.niton.jchad.model.User;
import com.niton.jchad.rest.model.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, MessageId> {
	List<Message> findByChatAndReceiveTimesIsNotContainingOrderBySendingTime(long chat,User u);
	List<Message> findByChatAndSendingTimeBetweenOrderBySendingTime(long chat,
	                                                                LocalDateTime from,
	                                                                LocalDateTime to);
	List<Message> findByChatOrderBySendingTime(long chat, PageRequest limit);

	Message findMessageByChatOrderBySendingTime(long chat);

	boolean existsBySendingTimeAndSenderAndChat(LocalDateTime msgSendingTime, String senderId, long chat);
	Message findBySendingTimeAndSenderAndChat(LocalDateTime msgSendingTime, String senderId, long chat);
}

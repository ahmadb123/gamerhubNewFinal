package com.Repository.MessagesRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.models.ChatsAndDirectMessages.DirectMessages;

public interface DirectMessageRepository extends JpaRepository<DirectMessages, Long> {
    List<DirectMessages> findBySessionId(Long sessionId);
}

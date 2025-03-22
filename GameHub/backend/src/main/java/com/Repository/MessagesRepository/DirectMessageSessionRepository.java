package com.Repository.MessagesRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.models.ChatsAndDirectMessages.DirectMessageSession;

public interface DirectMessageSessionRepository extends JpaRepository<DirectMessageSession, Long> {
    
    @Query("SELECT s FROM DirectMessageSession s " +
           "WHERE (s.userA.id = :userOneId AND s.userB.id = :userTwoId) " +
           "   OR (s.userA.id = :userTwoId AND s.userB.id = :userOneId)")
    Optional<DirectMessageSession> findSessionBetweenUsers(@Param("userOneId") Long userOneId, 
                                                           @Param("userTwoId") Long userTwoId);
}

// package com.Repository.MessagesRepository;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

// import com.models.ChatsAndDirectMessages.DirectMessageSession;

// public interface DirectMessageSessionRepository extends JpaRepository<DirectMessageSession, Long> {
    
//     @Query("SELECT s FROM DirectMessageSession s " +
//            "WHERE (s.userA.id = :userOneId AND s.userB.id = :userTwoId) " +
//            "   OR (s.userA.id = :userTwoId AND s.userB.id = :userOneId)")
//     Optional<DirectMessageSession> findSessionBetweenUsers(@Param("userOneId") Long userOneId, 
//                                                            @Param("userTwoId") Long userTwoId);

//     @Query("SELECT s FROM DirectMessageSession s " +
//        "JOIN s.participants p " +
//        "WHERE p.id IN (:userIds) " +
//        "GROUP BY s.id " +
//        "HAVING COUNT(p.id) = :size " +

//        "   AND COUNT(p.id) = (SELECT COUNT(u.id) FROM s.participants u)")

//     Optional<DirectMessageSession> findGroupSessionByParticipantIds(@Param("userIds") List<Long> userIds, @Param("size") Long size);
    
// }


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
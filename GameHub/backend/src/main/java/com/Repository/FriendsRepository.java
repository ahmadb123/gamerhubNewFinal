package com.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.models.FriendsModel.Friends;

public interface FriendsRepository extends JpaRepository<Friends, Long> {
    Optional<Friends> findByUserIdAndFriendId(Long userId , Long friendId);
    @Query("SELECT f FROM Friends f WHERE f.friend.id = :friendId AND f.status = 'pending'")
    List<Friends> findPendingRequestsForUser(@Param("friendId") Long friendId);

}

package com.Repository;
import com.models.UserSavedGames.MyGames;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface UserSavedGamesRepository extends JpaRepository<MyGames, Long> {
    Optional<MyGames> findById(Long id);
    
    @Query("select m.gameId from MyGames m where m.user.id = :userId")
    List<Long> findGameIdsByUserId(@Param("userId") Long userId);
}
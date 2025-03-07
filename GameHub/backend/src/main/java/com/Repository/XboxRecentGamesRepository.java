package com.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.models.DataModelAccountLinks.XboxRecentGame;

@Repository
public interface XboxRecentGamesRepository extends JpaRepository<XboxRecentGame, Long> {
    void deleteByTitleId(String titleId);
    void deleteByXboxProfileId(Long id);
    @Query("SELECT g FROM XboxRecentGame g "
    + "WHERE g.xboxProfile.user.username = :username")
    List<XboxRecentGame> findRecentGamesByUsername(@Param("username") String username);
}

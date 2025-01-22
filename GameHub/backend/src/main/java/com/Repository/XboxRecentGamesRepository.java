package com.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.models.DataModelAccountLinks.XboxRecentGame;

@Repository
public interface XboxRecentGamesRepository extends JpaRepository<XboxRecentGame, Long> {
    Optional<XboxRecentGame> findByTitleId(String titleId);
    void deleteByTitleId(String titleId);
}

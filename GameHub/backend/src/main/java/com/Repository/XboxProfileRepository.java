package com.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.models.XboxModel.XboxProfile;

@Repository
public interface XboxProfileRepository extends JpaRepository<XboxProfile, Long> {
    @Query("SELECT x FROM XboxProfile x WHERE x.user.id = :userId AND x.xboxGamertag = :gamertag")
    Optional<XboxProfile> findByUserIdAndGamertag(@Param("userId") Long userId, @Param("gamertag") String gamertag);
    @Query("SELECT x FROM XboxProfile x WHERE x.user.username = :username")
    Optional<XboxProfile> findXboxUserByUsername(@Param("username") String username);
    @Query("SELECT x FROM XboxProfile x WHERE x.user.id = :userId")
    Optional<XboxProfile> findByUserId(@Param("userId") Long userId);
}


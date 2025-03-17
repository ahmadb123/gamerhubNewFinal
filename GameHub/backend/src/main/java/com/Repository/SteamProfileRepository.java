package com.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.models.Steam.SteamUserProfile.SteamProfile;

@Service
public interface SteamProfileRepository extends JpaRepository<SteamProfile, Long> {
    @Query("SELECT x FROM SteamProfile x WHERE x.user.id = :userId AND x.personaname = :steamName")
    Optional<SteamProfile> findUserIdAndSteamName(@Param("userId") Long userId, @Param("steamName") String steamName);
    @Query("SELECT x FROM SteamProfile x WHERE x.user.username = :username")
    Optional<SteamProfile> findSteamUserByUsername(@Param("username") String username);
    @Query("SELECT x FROM SteamProfile x WHERE x.user.id = :userId")
    Optional<SteamProfile> findSteamUserByUserId(@Param("userId") Long userId);
    @Query("SELECT x FROM SteamProfile x WHERE x.steamid = :steamId")
    Optional<SteamProfile> findSteamUserBySteamId(@Param("steamId") String steamId);
    @Query("SELECT x FROM SteamProfile x WHERE x.user.id = :userId")
    List<SteamProfile> findAllUserId(@Param("userId") Long userId);
}

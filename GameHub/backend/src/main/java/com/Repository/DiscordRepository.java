package com.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.models.Discord.DiscordUserProfile;

public interface DiscordRepository extends JpaRepository<DiscordUserProfile, Long>{
    Optional<DiscordUserProfile> findByDiscordId(String discordId);
}

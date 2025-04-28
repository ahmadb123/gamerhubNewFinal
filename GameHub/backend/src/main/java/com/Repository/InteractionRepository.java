package com.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.models.CommunityInsight.Interaction;
import com.models.CommunityInsight.Interaction.InteractionType;

public interface InteractionRepository extends JpaRepository<Interaction, Long>{
    long countByPostIdAndType(Long postId, InteractionType type);
    boolean existsByPostIdAndUserIdAndType(Long postId, Long userId, InteractionType type);
    List<Interaction> findByPostIdAndTypeOrderByTimestampDesc(Long postId, InteractionType type);

}

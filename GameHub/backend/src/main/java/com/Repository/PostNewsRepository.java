package com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.models.CommunityInsight.PostNews;

@Repository
public interface PostNewsRepository extends JpaRepository<PostNews, Long> {
}
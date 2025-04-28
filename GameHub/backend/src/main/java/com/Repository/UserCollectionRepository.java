package com.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.models.UserSavedGames.UserSavedCollection.MyCollection;

@Repository
public interface UserCollectionRepository
    extends JpaRepository<MyCollection, Long> {

  @Query("""
    SELECT DISTINCT c
      FROM MyCollection c
      LEFT JOIN FETCH c.items
     WHERE c.user.id = :userId
    """)
  List<MyCollection> findByUserIdWithItems(@Param("userId") Long userId);
}

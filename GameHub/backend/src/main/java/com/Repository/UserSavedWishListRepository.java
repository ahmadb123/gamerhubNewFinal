package com.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.models.UserSavedGames.MyWishList;

public interface UserSavedWishListRepository extends JpaRepository<MyWishList, Long> {
    Optional<MyWishList> findById(Long id);
    
    @Query("select m.gameId from MyWishList m where m.user.id = :userId")
    List<Long> findWishlistGameIdsByUserId(@Param("userId") Long userId);    
}

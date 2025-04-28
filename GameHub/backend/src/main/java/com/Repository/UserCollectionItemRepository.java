package com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.models.UserSavedGames.UserSavedCollection.CollectionItem;

public interface UserCollectionItemRepository extends JpaRepository<CollectionItem,Long> {}

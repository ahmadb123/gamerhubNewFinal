package com.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Repository.UserCollectionItemRepository;
import com.Repository.UserCollectionRepository;
import com.Repository.UserRepository;
import com.models.UserModel.User;
import com.models.UserSavedGames.UserSavedCollection.CollectionItem;
import com.models.UserSavedGames.UserSavedCollection.MyCollection;
import com.utility.JWT;

@Service
public class CollectionService {
    @Autowired JWT jwt;
    @Autowired UserRepository users;
    @Autowired UserCollectionRepository collRepo;
    @Autowired UserCollectionItemRepository itemRepo;

    public List<MyCollection> listUserCollections(String token) {
        Long id = jwt.extractUserId(token);
        return collRepo.findByUserIdWithItems(id);
    }

    // collect friends collections by id 
    public List<MyCollection> listFriendsCollections(String token, Long friendId) {
        Long uid = jwt.extractUserId(token);
        if (uid.equals(friendId)) {
            return collRepo.findByUserIdWithItems(uid);
        }
        return collRepo.findByUserIdWithItems(friendId);
    }

    public MyCollection createCollection(String token, String name) {
        Long uid = jwt.extractUserId(token);
        User user = users.findById(uid).orElseThrow();
        MyCollection collection = new MyCollection();
        collection.setName(name); collection.setUser(user);
        return collRepo.save(collection);
    }

    public void addGameToCollection(String token, Long collId, Long gameId) {
        Long uid = jwt.extractUserId(token);
        MyCollection c = collRepo.findById(collId)
                .filter(x->x.getUser().getId().equals(uid))
                .orElseThrow();
        CollectionItem it = new CollectionItem();
        it.setCollection(c);
        it.setGameId(gameId);
        itemRepo.save(it);
    }
}

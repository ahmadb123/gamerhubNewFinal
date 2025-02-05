package com.services;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Repository.FriendsRepository;
import com.models.FriendsModel.Friends;
import com.models.UserModel.User;

@Service
public class FriendsService {

    @Autowired
    private FriendsRepository friendsRepository;

    public void sendFriendRequest(User requester, User targetUser){
        // create new friend request - 
        Friends friend = new Friends();
        friend.setUser(requester); // The user who is sending the request 
        friend.setFriend(targetUser); // The user who is receiving the request
        friend.setStatus("pending");
        friend.setCreatedAt(LocalDateTime.now());

        // save - 
        friendsRepository.save(friend);
    }

    public void acceptFriendRequest(Long friendRequestId){
        Optional<Friends> friend = friendsRepository.findById(friendRequestId);
        if(friend.isPresent()){
            Friends friendship = friend.get();
            friendship.setStatus("accepted");
            friendsRepository.save(friend.get());
        }
    }
}

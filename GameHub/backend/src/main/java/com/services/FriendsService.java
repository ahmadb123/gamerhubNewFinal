package com.services;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Repository.FriendsRepository;
import com.models.FriendsModel.Friends;
import com.models.UserModel.User;
import com.models.XboxModel.XboxProfile;

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

    // get all accepted or friends from the database - 
    public List<User>getAllFriends(Long userId){
        // save all friends in a list -
        List<Friends> allAcceptedFriends = 
                        friendsRepository.findAllAcceptedByUserId(userId);
        List<User> friendUserInfo = new ArrayList<>();
        for(Friends friend : allAcceptedFriends){
            if(friend.getUser().getId() == userId){
                friendUserInfo.add(friend.getFriend());
            }else{
                friendUserInfo.add(friend.getUser());
            }
        }
        return friendUserInfo;
    }

    // if user friend is xbox user, get their xbox profile -
    public List<XboxProfile> getXboxProfilesForFriends(Long userId){
        List<User> friends = getAllFriends(userId);
        List<XboxProfile> xboxProfiles = new ArrayList<>();
        // we need to check if user is xbox user- 
        for(User friend : friends){
            if(!friend.getXboxProfiles().isEmpty()){
                xboxProfiles.add(friend.getXboxProfiles().get(0));
            }else{
                continue;
            }
        }
        return xboxProfiles;
    }
}

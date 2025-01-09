package com.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Repository.PostNewsRepository;
import com.models.CommunityInsight.PostNews;
import com.models.UserModel.User;

@Service
public class PostNewsService {
    @Autowired
    private PostNewsRepository postNewsRepository;
   
    public void createPost(User user, PostNews postNews) {
    
    if(postNews.getTimeShared() == null){
        postNews.setTimeShared(LocalDateTime.now());
    }
    // Set the user to establish the relationship
    postNews.setUser(user);

    if (postNews.getContentText() == null || postNews.getContentText().isEmpty()) {
        throw new IllegalArgumentException("Content text cannot be empty");
    }
    // Save the updated PostNews object to the database
    postNewsRepository.save(postNews);
    }

}

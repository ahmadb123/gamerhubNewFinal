/*
 * This service class is responsible for handling the business logic for the PostNews model.
 * the class returns all posts from the db 
 * helper method to get shared_news_id / slug in a list if available
 * in order to call the external api to get the details of the shared news and enrich the posts
 */

package com.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.Repository.PostNewsRepository;
import com.dto.PostNewsDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.models.CommunityInsight.PostNews;
import com.models.UserModel.User;

@Service
public class PostNewsService {
    private static final String API_KEY = "c6558e8ad9d54b4ca3697d0e63ac86ad";
    private static final String BASE_URL = "https://api.rawg.io/api/games";

    @Autowired
    private PostNewsRepository postNewsRepository;

    // Create a post and save it in the database
    public void createPost(User user, PostNews postNews) {
        if (postNews.getTimeShared() == null) {
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

    // Fetch all posts from the database and convert them to DTOs
    public List<PostNewsDTO> getAllPosts() {
        List<PostNews> postNews = postNewsRepository.findAll();
        List<PostNewsDTO> postNewsDTOs = new ArrayList<>();
        for (PostNews post : postNews) {
            postNewsDTOs.add(new PostNewsDTO(post));
        }
        return postNewsDTOs;
    }

    // Helper method to get shared_news_id / slug in a list if available
    public List<String> getAllSharedNewsId() {
        List<PostNewsDTO> returnPostsResults = getAllPosts(); // Get all posts
        List<String> sharedNewsIds = new ArrayList<>();
        for (PostNewsDTO post : returnPostsResults) {
            if (post.getSharedNewsId() != null) {
                sharedNewsIds.add(post.getSharedNewsId());
            }
        }
        return sharedNewsIds;
    }

    // API call to get details of specific shared news and enrich posts
    public List<PostNewsDTO> getSharedNewsDetails() {
        List<PostNewsDTO> enrichedPosts = new ArrayList<>();
        List<PostNewsDTO> allPosts = getAllPosts(); // Get all posts to enrich

        RestTemplate restTemplate = new RestTemplate();

        for (PostNewsDTO post : allPosts) {
            String slugId = post.getSharedNewsId();
            if (slugId != null && !slugId.isEmpty()) {
                String url = String.format("%s/%s?key=%s", BASE_URL, slugId, API_KEY);

                try {
                    JsonNode response = restTemplate.getForObject(url, JsonNode.class);
                    if (response != null) {
                        // Enrich the existing PostNewsDTO with external data
                        post.setName(response.get("name").asText());
                        post.setReleased(response.get("released").asText());
                        post.setBackgroundImage(response.get("background_image").asText());
                    }
                } catch (Exception e) {
                    System.err.println("Error fetching details for slug: " + slugId);
                    e.printStackTrace();
                }
            }
            enrichedPosts.add(post);
        }

        return enrichedPosts;
    }
}

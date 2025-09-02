package com.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Repository.InteractionRepository;
import com.Repository.PostNewsRepository;
import com.Repository.UserRepository;
import com.models.CommunityInsight.Interaction;
import com.models.CommunityInsight.Interaction.InteractionType;
import com.models.CommunityInsight.PostNews;
import com.models.UserModel.User;

@Service
public class InteractionService {

    @Autowired 
    private InteractionRepository interactionRepository;

    @Autowired  
    private PostNewsRepository postNewsRepository;

    @Autowired 
    private UserRepository userRepository;

    /**
     * Toggle a like for the given post by the given user.
     * If a like exists, remove it; otherwise create it.
     * Returns the new total like count.
     */
    public long toggleLike(Long postId, Long userId) {
        boolean exists = interactionRepository
            .existsByPostIdAndUserIdAndType(postId, userId, InteractionType.LIKE);

        if (exists) {
            // remove the existing like
            List<Interaction> likes = interactionRepository
                .findByPostIdAndTypeOrderByTimestampDesc(postId, InteractionType.LIKE);

            for (Interaction like : likes) {
                if (like.getUser().getId().equals(userId)) {
                    interactionRepository.delete(like);
                    break;
                }
            }
        } else {
            // create a new like
            PostNews post = postNewsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + postId));
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

            Interaction like = new Interaction();
            like.setPost(post);
            like.setUser(user);
            like.setType(InteractionType.LIKE);
            like.setTimestamp(LocalDateTime.now());
            interactionRepository.save(like);
        }

        // return updated count
        return interactionRepository.countByPostIdAndType(postId, InteractionType.LIKE);
    }

    /**
     * Add a comment to the given post by the given user.
     * Returns the saved Interaction.
     */
    public Interaction addComment(Long postId, Long userId, String text) {
        PostNews post = postNewsRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("Post not found: " + postId));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Interaction comment = new Interaction();
        comment.setPost(post);
        comment.setUser(user);
        comment.setType(InteractionType.COMMENT);
        comment.setCommentText(text);
        comment.setTimestamp(LocalDateTime.now());
        return interactionRepository.save(comment);
    }


    public long countComments(Long postId) {
        return interactionRepository.countByPostIdAndType(postId, InteractionType.COMMENT);
    }

    // count likes - 
    public long countLikes(Long postId) {
        return interactionRepository.countByPostIdAndType(postId, InteractionType.LIKE);
    }
    /**
     * Fetch all comments for a post, newest first.
     */
    public List<Interaction> getComments(Long postId) {
        return interactionRepository
            .findByPostIdAndTypeOrderByTimestampDesc(postId, InteractionType.COMMENT);
    }
}

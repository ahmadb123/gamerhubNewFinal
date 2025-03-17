package com.example.demo.RepositoriesTest;

import static org.assertj.core.api.Assertions.assertThat;

import com.models.CommunityInsight.PostNews;
import com.models.UserModel.User;
import com.Repository.PostNewsRepository;
import com.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class PostNewsRepositoryTest {

    @Autowired
    private PostNewsRepository postNewsRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFindAll() {
        // Create and persist a user, because PostNews.user is required
        User user = new User();
        user.setUsername("testuser");
        // Set other required fields for the user if needed
        userRepository.save(user);
        
        PostNews post = new PostNews();
        post.setContentText("This is a test post.");
        post.setUser(user); // Set the non-null user

        PostNews saved = postNewsRepository.save(post);
        assertThat(saved.getId()).isNotNull();
        
        // Retrieve posts
        var posts = postNewsRepository.findAll();
        assertThat(posts).isNotEmpty();
    }
}

package com.example.demo.ServiceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.services.PostNewsService;
import com.Repository.PostNewsRepository;
import com.models.CommunityInsight.PostNews;
import com.models.UserModel.User;
import com.dto.PostNewsDTO;

public class PostNewsServiceTest {

    private PostNewsService service;
    private PostNewsRepository repository;

    @BeforeEach
    public void setUp() {
        service = new PostNewsService();
        repository = mock(PostNewsRepository.class);
        try {
            java.lang.reflect.Field field = PostNewsService.class.getDeclaredField("postNewsRepository");
            field.setAccessible(true);
            field.set(service, repository);
        } catch(Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }

    @Test
    public void testCreatePost() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        PostNews postNews = new PostNews();
        postNews.setContentText("Test post");
        
        service.createPost(user, postNews);
        assertNotNull(postNews.getTimeShared(), "Time shared should be set");
        assertEquals(user, postNews.getUser(), "User should be set in the post");
        verify(repository, times(1)).save(postNews);
    }
    
    @Test
    public void testGetAllPosts() {
        PostNews post = new PostNews();
        post.setContentText("Test post");
        User userMock = new User();
        userMock.setUsername("testuser");
        post.setUser(userMock);
        when(repository.findAll()).thenReturn(List.of(post));
        List<PostNewsDTO> dtos = service.getAllPosts();
        assertNotNull(dtos);
        assertFalse(dtos.isEmpty(), "Expected non-empty DTO list");
    }
}

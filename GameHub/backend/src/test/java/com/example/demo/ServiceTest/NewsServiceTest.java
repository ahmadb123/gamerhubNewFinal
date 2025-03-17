package com.example.demo.ServiceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import com.services.NewsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NewsServiceTest {

    private NewsService newsService;
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        newsService = new NewsService();
        restTemplate = mock(RestTemplate.class);
        // If NewsService creates its own RestTemplate, you might need to refactor to inject it.
        try {
            java.lang.reflect.Field field = NewsService.class.getDeclaredField("restTemplate");
            field.setAccessible(true);
            field.set(newsService, restTemplate);
        } catch(Exception e) {
            // If field not found, skip injection.
        }
    }

    @Test
    public void testGetNews_ValidResponse() throws Exception {
        String sampleJson = "{ \"results\": [ { \"id\": 1, \"name\": \"Game 1\", \"slug\": \"game-1\", \"released\": \"2021-01-01\", \"updated\": \"2021-01-02\", \"rating\": \"4.5\", \"rating_top\": \"5\", \"background_image\": \"http://example.com/image.png\" } ] }";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(sampleJson);
        when(restTemplate.getForObject(anyString(), eq(JsonNode.class))).thenReturn(jsonNode);

        List<?> newsList = newsService.getNews("2021-01-01", "2021-01-31", "1", null, "-rating", 10);
        assertNotNull(newsList);
        assertFalse(newsList.isEmpty(), "News list should not be empty for valid response");
    }
}

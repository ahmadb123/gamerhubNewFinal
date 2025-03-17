package com.example.demo.ServiceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import com.services.GameTrailerFromYoutubeService;
import com.models.YoutubeDataForGameVideos.YoutubeVideosModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GameTrailerFromYoutubeServiceTest {

    private GameTrailerFromYoutubeService service;
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        restTemplate = mock(RestTemplate.class);
        service = new GameTrailerFromYoutubeService(restTemplate);
    }

    @Test
    public void testGetGameTrailerFromYoutube_ValidResponse() throws Exception {
        String gameName = "Test Game";
        String sampleJson = "{ \"items\": [ { \"id\": { \"videoId\": \"abc123\", \"kind\": \"youtube#video\" }, \"snippet\": { \"title\": \"Test Trailer\", \"description\": \"Trailer description\", \"publishedAt\": \"2021-01-01T00:00:00Z\", \"channelId\": \"channel1\" } } ] }";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(sampleJson);
        when(restTemplate.getForObject(anyString(), eq(JsonNode.class))).thenReturn(jsonNode);

        YoutubeVideosModel model = service.getGameTrailerFromYoutube(gameName);
        assertNotNull(model);
        assertFalse(model.getItems().isEmpty(), "Expected at least one trailer item");
        assertEquals("abc123", model.getItems().get(0).getId().getVideoId());
    }
}

package com.example.demo.ControllerTest;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.controllers.NewsController;
import com.models.NewsModel.Genres;
import com.models.NewsModel.News;
import com.models.NewsModel.NewsResults;
import com.services.NewsService;
import com.utility.JWT;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = NewsController.class)
@AutoConfigureMockMvc(addFilters = false)  // Disable security filters for controller tests
public class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private NewsService newsService;
    
    @MockBean
    private JWT jwt;
    
    @Test
    public void testGetRecentNews_Success() throws Exception {
        // Stub newsService.getNews to return an empty list
        when(newsService.getNews(anyString(), anyString(), anyString(), isNull(), anyString(), anyInt()))
                .thenReturn(Collections.emptyList());
        
        // Perform GET /api/news/recent-news using defaults.
        mockMvc.perform(get("/api/news/recent-news"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }
    
    @Test
    public void testGetAllNews_Success() throws Exception {
        // Create a dummy NewsResults object with properties from your model.
        NewsResults dummyResult = new NewsResults();
        dummyResult.setName("Dummy News Title");
        dummyResult.setSlug("dummy-news-title");
        // You may set additional properties as needed
        
        // Create a dummy News wrapper and set the results list.
        News dummyNews = new News();
        dummyNews.setResults(Arrays.asList(dummyResult));
        List<News> newsList = Arrays.asList(dummyNews);
        
        // Stub newsService.getNews to return the dummy list.
        when(newsService.getNews(anyString(), anyString(), anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(newsList);
        
        // Perform GET /api/news/all-news with query parameters.
        mockMvc.perform(get("/api/news/all-news")
                .param("platform", "1")
                .param("genre", "action"))
            .andExpect(status().isOk())
            // Assert that the first News object's first NewsResults has the expected properties.
            .andExpect(jsonPath("$[0].results[0].name").value("Dummy News Title"))
            .andExpect(jsonPath("$[0].results[0].slug").value("dummy-news-title"));
    }
    
    @Test
    public void testGetGenres_Success() throws Exception {
        // Stub newsService.getAllGenres to return a list with one dummy genre.
        Genres dummyGenre = new Genres();
        dummyGenre.setName("Action");
        List<Genres> genres = Arrays.asList(dummyGenre);
        when(newsService.getAllGenres()).thenReturn(genres);
        
        mockMvc.perform(get("/api/news/genres"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Action"));
    }
    
    @Test
    public void testSearchGame_Success() throws Exception {
        // Create a dummy NewsResults object for search results.
        NewsResults dummyResult = new NewsResults();
        dummyResult.setName("Search Result News");
        dummyResult.setSlug("search-result-news");
        // Wrap it into a News object.
        News dummyNews = new News();
        dummyNews.setResults(Arrays.asList(dummyResult));
        List<News> newsList = Arrays.asList(dummyNews);
        when(newsService.searchForGame("TestGame")).thenReturn(newsList);
        
        mockMvc.perform(get("/api/news/search-game")
                .param("gameName", "TestGame"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].results[0].name").value("Search Result News"))
            .andExpect(jsonPath("$[0].results[0].slug").value("search-result-news"));
    }
    
    @Test
    public void testGetGameById_Success() throws Exception {
        // Stub newsService.getGameById to return a dummy NewsResults.
        NewsResults dummyResult = new NewsResults();
        dummyResult.setId(3L);
        dummyResult.setName("Game Detail News");
        dummyResult.setSlug("game-detail-news");
        when(newsService.getGameById(3L)).thenReturn(dummyResult);
        
        mockMvc.perform(get("/api/news/search-game-by-id/3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.name").value("Game Detail News"))
            .andExpect(jsonPath("$.slug").value("game-detail-news"));
    }
}

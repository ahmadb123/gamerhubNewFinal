package com.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.models.NewsModel.Genres;
import com.models.NewsModel.News;
import com.models.NewsModel.NewsResults;
import com.models.NewsModel.Platform;
import com.models.NewsModel.Platforms;
import com.models.NewsModel.ShortScreenShotNews;

@Service
public class NewsService {
    private static final String API_KEY = "c6558e8ad9d54b4ca3697d0e63ac86ad";
    private static final String BASE_URL = "https://api.rawg.io/api/games";
    public List<News> getNews(
        String startDate,
        String endDate,
        String platforms,
        String genre,
        String ordering,
        Integer pageSize
    ) {
        List<News> newsList = new ArrayList<>();
        try {
            // Base URL and mandatory API key
            StringBuilder urlBuilder = new StringBuilder(BASE_URL);
            urlBuilder.append("?key=").append(API_KEY);
    
            // If date range is provided
            if (startDate != null && endDate != null) {
                urlBuilder.append("&dates=").append(startDate).append(",").append(endDate);
            }
    
            // If platforms are provided (e.g., "4,18")
            if (platforms != null && !platforms.trim().isEmpty()) {
                urlBuilder.append("&platforms=").append(platforms);
            }
    
            // If genre is provided
            if (genre != null && !genre.trim().isEmpty()) {
                urlBuilder.append("&genres=").append(genre);
            }
    
            // If ordering is provided (e.g., "-rating", "-released", etc.)
            if (ordering != null && !ordering.trim().isEmpty()) {
                urlBuilder.append("&ordering=").append(ordering);
            }
    
            // If pageSize is provided
            if (pageSize != null && pageSize > 0) {
                urlBuilder.append("&page_size=").append(pageSize);
            } else {
                // Default page size if none specified
                urlBuilder.append("&page_size=20");
            }
    
            // Convert to string
            String url = urlBuilder.toString();
    
            // Make the request
            RestTemplate restTemplate = new RestTemplate();
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
    
            // Process JSON results
            if (response != null && response.has("results")) {
                for (JsonNode result : response.get("results")) {
                    News news = new News();
                    List<NewsResults> resultsList = new ArrayList<>();
    
                    NewsResults newsResults = new NewsResults();
                    newsResults.setName(result.get("name").asText());
                    newsResults.setSlug(result.get("slug").asText());
                    newsResults.setReleased(result.get("released").asText());
                    newsResults.setUpdated(result.get("updated").asText());
    
                    // Background image
                    if (result.has("background_image") && !result.get("background_image").isNull()) {
                        newsResults.setBackground_image(result.get("background_image").asText());
                    }
    
                    // Short screenshots
                    if (result.has("short_screenshots") && !result.get("short_screenshots").isNull()) {
                        for (JsonNode shot : result.get("short_screenshots")) {
                            ShortScreenShotNews shortScreenShotNews = new ShortScreenShotNews();
                            shortScreenShotNews.setImage(shot.get("image").asText());
                            newsResults.getShort_screenshots().add(shortScreenShotNews);
                        }
                    }
    
                    // Genres
                    if (result.has("genres") && !result.get("genres").isNull()) {
                        for (JsonNode genreNode : result.get("genres")) {
                            Genres genreObj = new Genres();
                            genreObj.setName(genreNode.get("name").asText());
                            newsResults.getGenres().add(genreObj);
                        }
                    }
    
                    // Platforms
                    if (result.has("platforms") && !result.get("platforms").isNull()) {
                        for (JsonNode platformNode : result.get("platforms")) {
                            Platform platform = new Platform();
                            Platforms allPlatforms = new Platforms();
    
                            JsonNode platformJson = platformNode.get("platform");
                            platform.setName(platformJson.get("name").asText());
                            platform.setSlug(platformJson.get("slug").asText());
    
                            allPlatforms.setPlatform(platform);
                            newsResults.getPlatforms().add(allPlatforms);
                        }
                    }
    
                    resultsList.add(newsResults);
                    news.setResults(resultsList);
                    newsList.add(news);
                }
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsList;
    }    

    // get all genres - 
    public List<Genres> getAllGenres(){
        List<Genres> genresList = new ArrayList<>();
        try{
            String url = "https://api.rawg.io/api/genres?key=" + API_KEY;
            RestTemplate restTemplate = new RestTemplate();
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            if(response != null && response.has("results")){
                for(JsonNode result : response.get("results")){
                    Genres genre = new Genres();
                    genre.setName(result.get("name").asText());
                    genre.setSlug(result.get("slug").asText());
                    genresList.add(genre);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return genresList;
    }
}

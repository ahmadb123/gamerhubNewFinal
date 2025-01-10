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

    public List<News> getRecentNews(String startDate, String endDate, String platforms){
        List<News> newsList = new ArrayList<>(); // will store the list of news
        try{
            String url = String.format(
                "%s?key=%s&dates=%s,%s&ordering=-relevance&page_size=10&platforms=%s",
                BASE_URL, API_KEY, startDate, endDate, platforms
            );
            // Fetch data from RAWG API
            RestTemplate restTemplate = new RestTemplate();
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            // process the response
            if(response != null && response.has("results")){
                for(int i = 0; i < response.get("results").size(); i++){
                    JsonNode result = response.get("results").get(i);
                    News news = new News();
                    List<NewsResults> resultsList = new ArrayList<>();
                    NewsResults newsResults = new NewsResults();
                    newsResults.setName(result.get("name").asText());
                    newsResults.setSlug(result.get("slug").asText());
                    newsResults.setReleased(result.get("released").asText());
                    newsResults.setUpdated(result.get("updated").asText());    
                    if(result.has("background_image") && !result.get("background_image").isNull()){
                        newsResults.setBackground_image(result.get("background_image").asText());
                    }
                    if(result.has("short_screenshots") && !result.get("short_screenshots").isNull()){
                        for(int j = 0; j < result.get("short_screenshots").size(); j++){
                            ShortScreenShotNews shortScreenShotNews = new ShortScreenShotNews();
                            shortScreenShotNews.setImage(result.get("short_screenshots").get(j).get("image").asText());
                            newsResults.getShort_screenshots().add(shortScreenShotNews);
                        }
                    }
                    if(result.has("genres") && !result.get("genres").isNull()){
                        for(int j = 0; j < result.get("genres").size(); j++){
                            Genres genres = new Genres(); 
                            genres.setName(result.get("genres").get(j).get("name").asText());
                            newsResults.getGenres().add(genres);
                        }
                    }
                    if(result.has("platforms") && !result.get("platforms").isNull()){
                        for(int j = 0; j < result.get("platforms").size();j++){
                            Platform platform = new Platform();
                            Platforms allPlatforms = new Platforms();
                            platform.setName(result.get("platforms").get(j).get("platform").get("name").asText());
                            platform.setSlug(result.get("platforms").get(j).get("platform").get("slug").asText());
                            allPlatforms.setPlatform(platform);
                            newsResults.getPlatforms().add(allPlatforms);
                        }
                    }
                    resultsList.add(newsResults);
                    news.setResults(resultsList);
                    newsList.add(news);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return newsList;
    }
}

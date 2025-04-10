package com.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.models.NewsModel.Developers;
import com.models.NewsModel.Genres;
import com.models.NewsModel.News;
import com.models.NewsModel.NewsResults;
import com.models.NewsModel.Platform;
import com.models.NewsModel.Platforms;
import com.models.NewsModel.Ratings;
import com.models.NewsModel.Requirement;
import com.models.NewsModel.ShortScreenShotNews;
import com.models.NewsModel.Store;
import com.models.NewsModel.Stores;

@Service
public class NewsService {
    @Autowired
    private SaveGamesService saveGamesService;
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
                    newsResults.setId(result.get("id").asLong());
                    newsResults.setName(result.get("name").asText());
                    newsResults.setSlug(result.get("slug").asText());
                    newsResults.setReleased(result.get("released").asText());
                    newsResults.setUpdated(result.get("updated").asText());
                    newsResults.setRating(Double.parseDouble(result.get("rating").asText()));
                    newsResults.setRatingTop(Integer.parseInt(result.get("rating_top").asText()));
    
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

                    if(result.has("ratings") && !result.get("ratings").isNull()){
                        List<Ratings> allRatings  = new ArrayList<>();
                        int totalCount = 0;
                        double totalScoreSum = 0;
                        for(JsonNode ratingNode : result.get("ratings")){
                            Ratings ratings = new Ratings();
                            int score = ratingNode.get("id").asInt();
                            int count = ratingNode.get("count").asInt();
                            ratings.setId(score);
                            ratings.setTitle(ratingNode.get("title").asText());
                            ratings.setCount(count);
                            ratings.setPercent(ratingNode.get("percent").asDouble());
                            allRatings.add(ratings);

                            totalCount += count;
                            totalScoreSum += score * count;
                        }
                        // calculate average rating
                        double averageRating = (totalCount > 0) ?  totalScoreSum / totalCount : 0;
                        String formattedAvgRating = String.format("%.2f", averageRating);
                        newsResults.setAverageRating(Double.parseDouble(formattedAvgRating));

                        // sort rating by count - 
                        allRatings.sort((r1, r2) -> r2.getCount() - r1.getCount());

                        // get only top 5 
                        int topCount = Math.min(5, allRatings.size());
                        for(int i = 0; i < topCount; i++){
                            newsResults.getRatings().add(allRatings.get(i));
                        }

                        newsResults.setRatingTop(topCount);
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
                    genre.setImage_background(result.get("image_background").asText());
                    genresList.add(genre);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return genresList;
    }

    public List<News> searchForGame(String gameName) {
        List<News> newsList = new ArrayList<>();
        try {
            // Use the base URL without the extra '?search'
            String baseUrl = "https://api.rawg.io/api/games";
            StringBuilder urlBuilder = new StringBuilder(baseUrl);
            urlBuilder.append("?key=").append(API_KEY);
            urlBuilder.append("&search=").append(gameName);
            String url = urlBuilder.toString();
    
            // make the request -
            RestTemplate restTemplate = new RestTemplate();
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
    
            // process the response -
            if (response != null && response.has("results")) {
                for (JsonNode result : response.get("results")) {
                    News news = new News();
                    List<NewsResults> resultsList = new ArrayList<>();
                    NewsResults newsResults = new NewsResults();
                    
                    newsResults.setName(result.get("name").asText());
                    newsResults.setSlug(result.get("slug").asText());
                    newsResults.setId(result.get("id").asLong());
                    newsResults.setPlayTime(Integer.parseInt(result.get("playtime").asText()));
                    newsResults.setUpdated(result.get("updated").asText());
                    newsResults.setRating(Double.parseDouble(result.get("rating").asText()));
                    newsResults.setRatingTop(Integer.parseInt(result.get("rating_top").asText()));
                    newsResults.setReleased(result.get("released").asText());
                    // background image
                    if (result.has("background_image") && !result.get("background_image").isNull()) {
                        newsResults.setBackground_image(result.get("background_image").asText());
                    }
                    
                    // short screenshots
                    if (result.has("short_screenshots") && !result.get("short_screenshots").isNull()) {
                        for (JsonNode shot : result.get("short_screenshots")) {
                            ShortScreenShotNews shortScreenShotNews = new ShortScreenShotNews();
                            shortScreenShotNews.setImage(shot.get("image").asText());
                            newsResults.getShort_screenshots().add(shortScreenShotNews);
                        }
                    }
                    
                    // genres
                    if (result.has("genres") && !result.get("genres").isNull()) {
                        for (JsonNode genreNode : result.get("genres")) {
                            Genres genreObj = new Genres();
                            genreObj.setName(genreNode.get("name").asText());
                            newsResults.getGenres().add(genreObj);
                        }
                    }
                    
                    // platforms
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
                    
                    if(result.has("stores") && !result.get("stores").isNull()){
                        for(JsonNode storeNode : result.get("stores")){
                            Stores stores = new Stores();
                            Store store = new Store();
                            JsonNode storeJson = storeNode.get("store");
                            store.setName(storeJson.get("name").asText());
                            store.setSlug(storeJson.get("slug").asText());
                            store.setId(storeJson.get("id").asInt());
                            stores.setStore(store);
                            newsResults.getStores().add(stores);
                        }
                    }
    
                    if(result.has("ratings") && !result.get("ratings").isNull()){
                        List<Ratings> allRatings  = new ArrayList<>();
                        int totalCount = 0;
                        double totalScoreSum = 0;
                        for(JsonNode ratingNode : result.get("ratings")){
                            Ratings ratings = new Ratings();
                            int score = ratingNode.get("id").asInt();
                            int count = ratingNode.get("count").asInt();
                            ratings.setId(score);
                            ratings.setTitle(ratingNode.get("title").asText());
                            ratings.setCount(count);
                            ratings.setPercent(ratingNode.get("percent").asDouble());
                            allRatings.add(ratings);

                            totalCount += count;
                            totalScoreSum += score * count;
                        }
                        // calculate average rating
                        double averageRating = (totalCount > 0) ?  totalScoreSum / totalCount : 0;
                        String formattedAvgRating = String.format("%.2f", averageRating);
                        newsResults.setAverageRating(Double.parseDouble(formattedAvgRating));

                        // sort rating by count - 
                        allRatings.sort((r1, r2) -> r2.getCount() - r1.getCount());

                        // get only top 5 
                        int topCount = Math.min(5, allRatings.size());
                        for(int i = 0; i < topCount; i++){
                            newsResults.getRatings().add(allRatings.get(i));
                        }

                        newsResults.setRatingTop(topCount);
                    }
                    
                    resultsList.add(newsResults);
                    news.setResults(resultsList);
                    newsList.add(news);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return newsList;
    }

    // get game by id = 
    public NewsResults getGameById(long gameId){
        try{
            // Fix URL string concatenation: added slash after "games"
            String url = "https://api.rawg.io/api/games/" + gameId + "?key=" + API_KEY;
            RestTemplate restTemplate = new RestTemplate();
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            // since this returns only one game we can directly return the response
            if(response != null && response.size() > 0){
                NewsResults newsResult = new NewsResults();
                newsResult.setId(response.get("id").asLong());
                newsResult.setName(response.get("name").asText());
                newsResult.setSlug(response.get("slug").asText());
                newsResult.setDesc(response.get("description").asText());
                newsResult.setUpdated(response.get("updated").asText());
                newsResult.setRating(response.get("rating").asDouble());
                newsResult.setRatingTop(response.get("rating_top").asInt());
                newsResult.setBackground_image(response.get("background_image").asText());
                newsResult.setWebsite(response.get("website").asText());    
                newsResult.setBackground_image_additional(response.get("background_image_additional").asText());
                newsResult.setMetacritic_url(response.get("metacritic_url").asText());
                newsResult.setAdded(response.get("added").asInt());
                newsResult.setReviews_count(response.get("reviews_count").asInt());
                newsResult.setReleased(response.get("released").asText());
                // loop over ratings 
                if(response.has("ratings") && !response.get("ratings").isNull()){
                    List<Ratings> allRatings  = new ArrayList<>();
                    int totalCount = 0;
                    double totalScoreSum = 0;
                    for(JsonNode ratingNode : response.get("ratings")){
                        Ratings ratings = new Ratings();
                        int score = ratingNode.get("id").asInt();
                        int count = ratingNode.get("count").asInt();
                        ratings.setId(score);
                        ratings.setTitle(ratingNode.get("title").asText());
                        ratings.setCount(count);
                        ratings.setPercent(ratingNode.get("percent").asDouble());
                        allRatings.add(ratings);
                        totalCount += count;
                        totalScoreSum += score * count;
                    }
                    double averageRating = (totalCount > 0) ?  totalScoreSum / totalCount : 0;
                    newsResult.setAverageRating(averageRating);
                    allRatings.sort((r1, r2) -> r2.getCount() - r1.getCount());
                    int topCount = Math.min(5, allRatings.size());
                    for(int i = 0; i < topCount; i++){
                        newsResult.getRatings().add(allRatings.get(i));
                    }
                    newsResult.setRatingTop(topCount);
                }
                if(response.has("genres") && !response.get("genres").isNull()){
                    for(JsonNode genreNode : response.get("genres")){
                        Genres genreObj = new Genres();
                        genreObj.setId(genreNode.get("id").asInt());
                        genreObj.setName(genreNode.get("name").asText());
                        genreObj.setSlug(genreNode.get("slug").asText());
                        genreObj.setImage_background(genreNode.get("image_background").asText());
                        newsResult.getGenres().add(genreObj);
                    }
                }
                if(response.has("platforms") && !response.get("platforms").isNull()){
                    for(JsonNode platformNode : response.get("platforms")){
                        Platform platform = new Platform();
                        Platforms allPlatforms = new Platforms();
                        JsonNode platformJson = platformNode.get("platform");
                        platform.setName(platformJson.get("name").asText());
                        platform.setSlug(platformJson.get("slug").asText());
                        allPlatforms.setPlatform(platform);
                        JsonNode releaseDate = platformNode.get("released_at");
                        if(releaseDate != null && !releaseDate.isNull()){
                            allPlatforms.setReleased_at(releaseDate.asText());
                        }
                        if(platformNode.has("requirements") && !platformNode.get("requirements").isNull()){
                           Requirement requirements = new Requirement();
                            JsonNode requirementNode = platformNode.get("requirements");
                            if(requirementNode.has("minimum") && !requirementNode.get("minimum").isNull()){
                                requirements.setMinimum(requirementNode.get("minimum").asText());
                            }
                            if(requirementNode.has("recommended") && !requirementNode.get("recommended").isNull()){
                                requirements.setRecommended(requirementNode.get("recommended").asText());
                            }
                            allPlatforms.setRequirements(requirements);
                        }
                        newsResult.getPlatforms().add(allPlatforms);
                    }
                }
                if(response.has("developers") && !response.get("developers").isNull()){
                    for(JsonNode developerNode : response.get("developers")){
                        Developers developers = new Developers();
                        developers.setId(developerNode.get("id").asInt());
                        developers.setName(developerNode.get("name").asText());
                        developers.setSlug(developerNode.get("slug").asText());
                        developers.setGames_count(developerNode.get("games_count").asInt());
                        developers.setImage_background(developerNode.get("image_background").asText());
                        newsResult.getDevelopers().add(developers);
                    }
                }
                return newsResult;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /*
     * pass user id so when we make this api call we pass it the correct user
     * then the savedgameid will extract the game id from the user id
     */
    // return news or game details by id - 
    public List<NewsResults> getSavedGameDetails(Long userId) {
        List<Long> savedGameIds = saveGamesService.getSavedGameIds(userId);
        List<NewsResults> gameDetailsList = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        
        for (Long gameId : savedGameIds) {
            try {
                String url = "https://api.rawg.io/api/games/" + gameId + "?key=" + API_KEY;
                JsonNode response = restTemplate.getForObject(url, JsonNode.class);
                
                if (response != null) {
                    NewsResults gameDetails = new NewsResults();
                    gameDetails.setId(response.get("id").asLong());
                    gameDetails.setName(response.get("name").asText());
                    gameDetails.setSlug(response.get("slug").asText());
                    gameDetails.setReleased(response.get("released").asText());  
                    gameDetails.setBackground_image(response.get("background_image").asText());                  
                    gameDetailsList.add(gameDetails);
                }
            } catch (Exception e) {
                // Handle exceptions for individual game fetches
                e.printStackTrace();
            }
        }
        
        return gameDetailsList;
    }
}
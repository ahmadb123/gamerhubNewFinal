package com.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.models.YoutubeDataForGameVideos.Id;
import com.models.YoutubeDataForGameVideos.Items;
import com.models.YoutubeDataForGameVideos.Snippet;
import com.models.YoutubeDataForGameVideos.YoutubeVideosModel;

@Service
public class GameTrailerFromYoutubeService {
    public YoutubeVideosModel getGameTrailerFromYoutube(String gameName) {
        YoutubeVideosModel youtubeVideosModel = new YoutubeVideosModel();
        List<Items> items = new ArrayList<>();
        // This method will return the game trailer from YouTube
        try {
            // params for the url - 
            /*
             * * part: Determines the resource parts returned. Use snippet to get details like title, description, and thumbnails.
             * q: Your search query. For game trailers, format it like "Game Title official trailer".
             * type: Set to video to limit results to videos.
             * maxResults: How many items to return (often 1 or a few).
             * key: Your API key.
             */
            String part = "snippet";
            // URL-encode the query string to handle spaces and special characters
            String q = URLEncoder.encode(gameName + " official trailer", StandardCharsets.UTF_8.toString());
            String type = "video";
            String maxResults = "1";
            String apiKey = "AIzaSyDDtdbmwUp-Syj___LRYSfkedx6iNSZkxg";
            String baseUrl = "https://www.googleapis.com/youtube/v3/search";
            String url = baseUrl + "?part=" + part + "&q=" + q + "&type=" + type + "&maxResults=" + maxResults + "&key=" + apiKey;

            // call the api
            RestTemplate restTemplate = new RestTemplate();
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            // Ensure that the response and its "items" array exist and have at least one item.
            if (response != null && response.has("items") && response.get("items").size() > 0) {
                JsonNode firstItem = response.get("items").get(0);
                
                // Extract the video ID
                Id videoId = new Id();
                JsonNode idNode = firstItem.get("id");
                if (idNode != null) {
                    videoId.setVideoId(idNode.get("videoId").asText());
                    videoId.setKind(idNode.get("kind").asText());
                }
                
                // Extract the snippet
                Snippet snippet = new Snippet();
                JsonNode snippetNode = firstItem.get("snippet");
                if (snippetNode != null) {
                    snippet.setTitle(snippetNode.get("title").asText());
                    snippet.setDescription(snippetNode.get("description").asText());
                    snippet.setPublishedAt(snippetNode.get("publishedAt").asText());
                    snippet.setChannelId(snippetNode.get("channelId").asText());
                }
                
                // Create the Items object and set both video ID and snippet
                Items item = new Items();
                item.setId(videoId);
                item.setSnippet(snippet);
                items.add(item);
                
                youtubeVideosModel.setItems(items);
                return youtubeVideosModel;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

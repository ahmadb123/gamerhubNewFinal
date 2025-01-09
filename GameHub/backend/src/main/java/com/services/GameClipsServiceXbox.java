package com.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.GameClipRecordXbox.GameClip;
import com.models.GameClipRecordXbox.GameClips;
import com.models.GameClipRecordXbox.GameClipsUri;
import com.models.GameClipRecordXbox.Thumbnail;
@Service
public class GameClipsServiceXbox {
    // use mapper to map the response - 

    public List<GameClips> getGameClips(String responseBody){
        List<GameClips> gameClips = new ArrayList<>(); // will store gameclips data objects
        // get the response from the API
        // map the response to the GameClips object
        // return the GameClips object
        try{
            if(responseBody == null){
                return null;
            }
            // use object mapper - 
            ObjectMapper mapper = new ObjectMapper();
            JsonNode response = mapper.readTree(responseBody);
            if(response != null && response.has("gameClips")){
                for(int i = 0; i < response.get("gameClips").size(); i++){
                    JsonNode gameClipsResults = response.get("gameClips").get(i);
                    GameClips gameClipsObjc = new GameClips();
                    List<GameClip> gameClipsList = new ArrayList<>();
                    GameClip gameClip = new GameClip();
                    gameClip.setGameClipId(gameClipsResults.get("gameClipId").asText());
                    gameClip.setDatePublished(gameClipsResults.get("datePublished").asText());
                    gameClip.setDateRecorded(gameClipsResults.get("dateRecorded").asText());
                    gameClip.setViews(Integer.parseInt(gameClipsResults.get("views").asText()));
                    gameClip.setSavedByUser(gameClipsResults.get("savedByUser").asBoolean());
                    gameClip.setTitleName(gameClipsResults.get("titleName").asText());

                    // hanlde thumbnails and gameClipUris
                    List<Thumbnail> thumbnailsList = new ArrayList<>();
                    if(gameClipsResults.has("thumbnails") && !gameClipsResults.get("thumbnails").isNull()){
                        for(int j = 0; j < gameClipsResults.get("thumbnails").size(); j++){
                            Thumbnail thumbnails = new Thumbnail();
                            thumbnails.setUri(gameClipsResults.get("thumbnails").get(j).get("uri").asText());
                            thumbnailsList.add(thumbnails);
                        }
                    }
                    gameClip.setThumbnails(thumbnailsList);

                    List<GameClipsUri> gameClipUrisList = new ArrayList<>();
                    if(gameClipsResults.has("gameClipUris") && !gameClipsResults.get("gameClipUris").isNull()){
                        for(int j = 0; j < gameClipsResults.get("gameClipUris").size(); j++){
                            GameClipsUri gameClipUris = new GameClipsUri();
                            gameClipUris.setUri(gameClipsResults.get("gameClipUris").get(j).get("uri").asText());
                            gameClipUrisList.add(gameClipUris);
                        }
                    }
                    gameClip.setGameClipUris(gameClipUrisList);

                    gameClipsList.add(gameClip);
                    gameClipsObjc.setGameClips(gameClipsList);
                    gameClips.add(gameClipsObjc);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return gameClips;
    }
}

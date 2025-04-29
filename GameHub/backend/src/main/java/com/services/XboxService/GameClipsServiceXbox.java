package com.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.GameClipRecordXbox.GameClip;
import com.models.GameClipRecordXbox.GameClipsUri;
import com.models.GameClipRecordXbox.Thumbnail;
// GameClipsServiceXbox.java
@Service
public class GameClipsServiceXbox {

    public List<GameClip> getGameClips(String responseBody){
        List<GameClip> clips = new ArrayList<>();
        if (responseBody == null) return clips;

        try {
            JsonNode root = new ObjectMapper().readTree(responseBody);
            for (JsonNode node : root.path("gameClips")) {
                GameClip clip = new GameClip();
                clip.setGameClipId(    node.path("gameClipId").asText());
                clip.setDatePublished( node.path("datePublished").asText());
                clip.setDateRecorded(  node.path("dateRecorded").asText());
                clip.setViews(         node.path("views").asInt());
                clip.setSavedByUser(   node.path("savedByUser").asBoolean());
                clip.setTitleName(     node.path("titleName").asText());

                // thumbnails
                List<Thumbnail> thumbs = new ArrayList<>();
                for (JsonNode t : node.path("thumbnails")) {
                    Thumbnail thumb = new Thumbnail();
                    thumb.setUri(t.path("uri").asText());
                    thumbs.add(thumb);
                }
                clip.setThumbnails(thumbs);

                // clip URIs
                List<GameClipsUri> uris = new ArrayList<>();
                for (JsonNode u : node.path("gameClipUris")) {
                    GameClipsUri uri = new GameClipsUri();
                    uri.setUri(u.path("uri").asText());
                    uris.add(uri);
                }
                clip.setGameClipUris(uris);

                clips.add(clip);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clips;
    }
}

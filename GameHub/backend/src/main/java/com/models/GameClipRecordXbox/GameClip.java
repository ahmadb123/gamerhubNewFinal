package com.models.GameClipRecordXbox;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameClip {
    private String gameClipId;
    private String datePublished;
    private String dateRecorded;
    private int views;
    private Boolean savedByUser;
    private List<Thumbnail> Thumbnails = new ArrayList<>();
    private List<GameClipsUri> gameClipUris = new ArrayList<>();
    private String titleName; // game title of clip

    public String getGameClipId() {
        return gameClipId;
    }

    public void setGameClipId(String gameClipId) {
        this.gameClipId = gameClipId;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public String getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(String dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public Boolean getSavedByUser() {
        return savedByUser;
    }

    public void setSavedByUser(Boolean savedByUser) {
        this.savedByUser = savedByUser;
    }

    public List<Thumbnail> getThumbnails() {
        return Thumbnails;
    }

    public void setThumbnails(List<Thumbnail> thumbnails) {
        Thumbnails = thumbnails;
    }

    public List<GameClipsUri> getGameClipUris() {
        return gameClipUris;
    }

    public void setGameClipUris(List<GameClipsUri> gameClipUris) {
        this.gameClipUris = gameClipUris;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }
}

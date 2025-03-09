package com.models.YoutubeDataForGameVideos;

import java.util.List;

public class YoutubeVideosModel {
    private List<Items> items  ; // array of items objects

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }
}

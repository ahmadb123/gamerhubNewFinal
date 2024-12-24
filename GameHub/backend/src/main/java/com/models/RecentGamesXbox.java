package com.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecentGamesXbox {
    private String xuid;
    private List<TitleId> titles;

    public String getXuid() {
        return xuid;
    }

    public void setXuid(String xuid) {
        this.xuid = xuid;
    }

    public List<TitleId> getTitles() {
        return titles;
    }

    public void setTitles(List<TitleId> titles) {
        this.titles = titles;
    }
}

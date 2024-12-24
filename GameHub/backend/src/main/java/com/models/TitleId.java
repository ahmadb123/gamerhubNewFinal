package com.models;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class TitleId {
    private String titleId;
    private String pfn;
    private String name;
    private String type;
    private List<String> devices;
    private String displayImage;
    private GameLastPlayed titleHistory;
    private GameDetails detail;

    // Getters and Setters
    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getPfn() {
        return pfn;
    }

    public void setPfn(String pfn) {
        this.pfn = pfn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getDevices() {
        return devices;
    }

    public void setDevices(List<String> devices) {
        this.devices = devices;
    }

    public String getDisplayImage() {
        return displayImage;
    }

    public void setDisplayImage(String displayImage) {
        this.displayImage = displayImage;
    }

    public GameLastPlayed getTitleHistory() {
        return titleHistory;
    }

    public void setTitleHistory(GameLastPlayed titleHistory) {
        this.titleHistory = titleHistory;
    }

    public GameDetails getDetail() {
        return detail;
    }

    public void setDetail(GameDetails detail) {
        this.detail = detail;
    }
}

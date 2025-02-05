package com.models.XboxProfileAchievements;

import java.util.List;

public class Achievements {
    private String id; 
    private List<TitleAssociations> titleAssociations;
    private Progression progression;
    private List<MediaAssets> mediaAssets; 
    private String description;
    
    public Achievements(String id, List<TitleAssociations> titleAssociations, Progression progression,  List<MediaAssets> mediaAssets, String description) {
    this.id = id;
    this.titleAssociations = titleAssociations;
    this.progression = progression;
    this.mediaAssets = mediaAssets;
    this.description = description;
}

    public Achievements() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TitleAssociations> getTitleAssociations() {
        return titleAssociations;
    }


    public void setTitleAssociations(List<TitleAssociations> titleAssociations) {
        this.titleAssociations = titleAssociations;
    }

    public Progression getProgression() {
        return progression;
    }

    public void setProgression(Progression progression) {
        this.progression = progression;
    }

    public List<MediaAssets> getMediaAssets() {
        return mediaAssets;
    }

    public void setMediaAssets(List<MediaAssets> mediaAssets) {
        this.mediaAssets = mediaAssets;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

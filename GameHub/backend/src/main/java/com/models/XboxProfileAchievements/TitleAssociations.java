package com.models.XboxProfileAchievements;

public class TitleAssociations {
    private String name;
    private String id;

    public TitleAssociations() {
    }
    
    public TitleAssociations(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

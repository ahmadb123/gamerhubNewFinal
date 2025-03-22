package com.models.NewsModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Platforms {
    private Platform platform; // Nested platform object
    private String released_at; // Release date of the game on this platform
    private Requirement requirements; // Requirements for the game on this platform
    // Getters and setters
    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getReleased_at() {
        return released_at;
    }

    public void setReleased_at(String released_at) {
        this.released_at = released_at;
    }

    public Requirement getRequirements() {
        return requirements;
    }

    public void setRequirements(Requirement requirements) {
        this.requirements = requirements;
    }
}

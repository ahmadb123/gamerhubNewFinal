package com.models.DataModelAccountLinks;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.models.XboxModel.XboxProfile;
@JsonIgnoreProperties(ignoreUnknown = true)

@Entity
@Table(name = "xbox_recent_games")
public class XboxRecentGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one link to the user's XboxProfile
    // so we know which user (profile) these games belong to
    @ManyToOne
    @JoinColumn(name = "xbox_profile_id", nullable = false)
    private XboxProfile xboxProfile;

    // Basic fields
    private String titleId;
    private String gameName;
    private String type;
    private String displayImage;

    // Example storing last-time-played
    private LocalDateTime lastTimePlayed;
    private String lastTimePlayedFormatted;

    @Lob
    private String description;

    @Lob
    private String shortDescription;

    private LocalDateTime releaseDate;

    public XboxRecentGame() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public XboxProfile getXboxProfile() {
        return xboxProfile;
    }

    public void setXboxProfile(XboxProfile xboxProfile) {
        this.xboxProfile = xboxProfile;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplayImage() {
        return displayImage;
    }

    public void setDisplayImage(String displayImage) {
        this.displayImage = displayImage;
    }

    public LocalDateTime getLastTimePlayed() {
        return lastTimePlayed;
    }

    public void setLastTimePlayed(LocalDateTime lastTimePlayed) {
        this.lastTimePlayed = lastTimePlayed;
    }

    public String getLastTimePlayedFormatted() {
        return lastTimePlayedFormatted;
    }

    public void setLastTimePlayedFormatted(String lastTimePlayedFormatted) {
        this.lastTimePlayedFormatted = lastTimePlayedFormatted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }
}
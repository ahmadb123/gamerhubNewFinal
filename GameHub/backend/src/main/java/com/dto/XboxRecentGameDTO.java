package com.dto;

public class XboxRecentGameDTO {
    private Long id;
    private String gameName;
    private String titleId;
    private String displayImage;
    private String lastTimePlayedFormatted;

    public XboxRecentGameDTO(Long id, String gameName, String titleId, String displayImage, String lastTimePlayedFormatted) {
        this.id = id;
        this.gameName = gameName;
        this.titleId = titleId;
        this.displayImage = displayImage;
        this.lastTimePlayedFormatted = lastTimePlayedFormatted;
    }

    public Long getId() {
        return id;
    }

    public String getGameName() {
        return gameName;
    }

    public String getTitleId() {
        return titleId;
    }

    public String getDisplayImage() {
        return displayImage;
    }

    public String getLastTimePlayedFormatted() {
        return lastTimePlayedFormatted;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public void setDisplayImage(String displayImage) {
        this.displayImage = displayImage;
    }

    public void setLastTimePlayedFormatted(String lastTimePlayedFormatted) {
        this.lastTimePlayedFormatted = lastTimePlayedFormatted;
    }
}

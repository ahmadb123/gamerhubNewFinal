package com.models.XboxModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.utility.TimeConverter;
@JsonIgnoreProperties(ignoreUnknown = true)

public class GameLastPlayed {
    private String lastTimePlayed;

    // Getters and Setters
    public String getLastTimePlayed() {
        return lastTimePlayed;
    }

    public void setLastTimePlayed(String lastTimePlayed) {
        this.lastTimePlayed = lastTimePlayed;
    }

    public String getLastTimePlayedFormatted() {
        return TimeConverter.convertToReadableFormat(lastTimePlayed);
    }
}

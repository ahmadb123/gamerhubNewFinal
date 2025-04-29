package com.models.PsnModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Top‐level container for a PSN recent‐games response.
 */
public class PsnRecentGames {
    @JsonProperty("recentTitles")
    private List<PsnRecentGame> recentTitles;

    public List<PsnRecentGame> getRecentTitles() {
        return recentTitles;
    }

    public void setRecentTitles(List<PsnRecentGame> recentTitles) {
        this.recentTitles = recentTitles;
    }
}

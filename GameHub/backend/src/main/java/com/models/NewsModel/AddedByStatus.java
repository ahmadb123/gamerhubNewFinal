package com.models.NewsModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class AddedByStatus {
    private int yet; 
    private int owned;
    private int beaten;
    private int toplay; // to play
    private int dropped;
    private int playing; // currently playing

    // Getters and Setters

    public int getYet() {
        return yet;
    }

    public void setYet(int yet) {
        this.yet = yet;
    }

    public int getOwned() {
        return owned;
    }

    public void setOwned(int owned) {
        this.owned = owned;
    }

    public int getBeaten() {
        return beaten;
    }

    public void setBeaten(int beaten) {
        this.beaten = beaten;
    }

    public int getToplay() {
        return toplay;
    }

    public void setToplay(int toplay) {
        this.toplay = toplay;
    }

    public int getDropped() {
        return dropped;
    }

    public void setDropped(int dropped) {
        this.dropped = dropped;
    }

    public int getPlaying() {
        return playing;
    }

    public void setPlaying(int playing) {
        this.playing = playing;
    }
}

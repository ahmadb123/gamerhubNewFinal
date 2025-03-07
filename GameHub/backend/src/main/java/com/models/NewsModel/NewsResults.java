package com.models.NewsModel;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsResults {
    private String name; // Title of the news
    private String slug; // name of the game
    private long id; // id of the game
    private int playTime; // Playtime of the game
    private List<Platforms> platforms = new ArrayList<>(); // Platforms of the game
    private List<Stores> stores = new ArrayList<>(); // Stores of the game
    private String released; // Release date of the game
    private String background_image; // Background image of the game
    private double rating; // Rating of the game
    private String updated; // Last updated date of the game
    private List <ShortScreenShotNews> short_screenshots = new ArrayList<>();
    private List <Genres> genres = new ArrayList<>(); // Genres of the game
    private int ratingTop; // Rating top of the game
    private List<Ratings> ratings = new ArrayList<>(); // Ratings of the game

    // Getters and Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public int getRatingTop() {
        return ratingTop;
    }

    public void setRatingTop(int ratingTop) {
        this.ratingTop = ratingTop;
    }

    public String getSlug(){
        return slug;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public List<Platforms> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platforms> platforms) {
        this.platforms = platforms;
    }

    public List<Stores> getStores() {
        return stores;
    }

    public void setStores(List<Stores> stores) {
        this.stores = stores;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getBackground_image() {
        return background_image;
    }

    public void setBackground_image(String background_image) {
        this.background_image = background_image;
    }

    public List<Ratings> getRatings() {
        return ratings;
    }

    public void setRatings(List<Ratings> ratings) {
        this.ratings = ratings;
    }
    
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public void setSlug(String slug){
        this.slug = slug;
    }

    public List<ShortScreenShotNews> getShort_screenshots() {
        return short_screenshots;
    }

    public void setShort_screenshots(List<ShortScreenShotNews> short_screenshots) {
        this.short_screenshots = short_screenshots;
    }

    public List<Genres> getGenres() {
        return genres;
    }

    public void setGenres(List<Genres> genres) {
        this.genres = genres;
    }
}

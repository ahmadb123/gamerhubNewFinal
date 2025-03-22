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
    private double averageRating; // Average rating of the game
    private List <ShortScreenShotNews> short_screenshots = new ArrayList<>();
    private List <Genres> genres = new ArrayList<>(); // Genres of the game
    private int ratingTop; // Rating top of the game
    private String desc; // Description of the game
    private List<Ratings> ratings = new ArrayList<>(); // Ratings of the game
    private String background_image_additional; // Additional background image of the game
    private String website;  // game website official link
    private List<AddedByStatus> addedByStatus = new ArrayList<>(); // added by status
    private List<Developers> developers = new ArrayList<>(); // developers of the game
    // for testing - 
    private String setContentText;
    private int added; // number of times the game has been added to the library
    private String metacritic_url; // metacritic url of the game
    private int reviews_count; // number of reviews for the game
    // Getters and Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMetacritic_url() {
        return metacritic_url;
    }

    public void setMetacritic_url(String metacritic_url) {
        this.metacritic_url = metacritic_url;
    }

    public String getSetContentText() {
        return setContentText;
    }


    public double getAverageRating() {
        return averageRating;
    }

    public void setContentText(String setContentText) {
        this.setContentText = setContentText;
    }

    public String getContentText() {
        return setContentText;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getAdded() {
        return added;
    }

    public void setAdded(int added) {
        this.added = added;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setWebsite(String website){
        this.website = website;
    }

    public String getWebsite(){
        return website;
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

    public int getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(int reviews_count) {
        this.reviews_count = reviews_count;
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

    public String getBackground_image_additional() {
        return background_image_additional;
    }

    public void setBackground_image_additional(String background_image_additional) {
        this.background_image_additional = background_image_additional;
    }

    public List<AddedByStatus> getAddedByStatus() {
        return addedByStatus;
    }

    public void setAddedByStatus(List<AddedByStatus> addedByStatus) {
        this.addedByStatus = addedByStatus;
    }

    public List<Developers> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<Developers> developers) {
        this.developers = developers;
    }

}

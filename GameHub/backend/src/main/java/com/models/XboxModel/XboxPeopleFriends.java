package com.models.XboxModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.models.NewsModel.Person;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XboxPeopleFriends {
    private Person[] people; // Array of people/friends
    private String recommendationSummary; // Recommendation summary
    private String friendFinderState; // Friend Finder state
    private String accountLinkDetails; // Account link details

    // Default constructor
    public XboxPeopleFriends() {}

    // Constructor with fields
    public XboxPeopleFriends(Person[] people, String recommendationSummary, String friendFinderState, String accountLinkDetails) {
        this.people = people;
        this.recommendationSummary = recommendationSummary;
        this.friendFinderState = friendFinderState;
        this.accountLinkDetails = accountLinkDetails;
    }

    // Getters and Setters
    public Person[] getPeople() {
        return people;
    }

    public void setPeople(Person[] people) {
        this.people = people;
    }

    public String getRecommendationSummary() {
        return recommendationSummary;
    }

    public void setRecommendationSummary(String recommendationSummary) {
        this.recommendationSummary = recommendationSummary;
    }

    public String getFriendFinderState() {
        return friendFinderState;
    }

    public void setFriendFinderState(String friendFinderState) {
        this.friendFinderState = friendFinderState;
    }

    public String getAccountLinkDetails() {
        return accountLinkDetails;
    }

    public void setAccountLinkDetails(String accountLinkDetails) {
        this.accountLinkDetails = accountLinkDetails;
    }
}

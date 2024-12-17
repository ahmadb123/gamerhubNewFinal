package com.mydomain.GamerHubBackEnd.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class xboxLoginRequest {
    // varibales 
    @JsonProperty("XboxToken")
    private String xboxToken;

    @JsonProperty("CreateAccount")
    private boolean createAccount;

    @JsonProperty("CustomTags")
    private Object customTags;

    @JsonProperty("InfoRequestParameters")
    private Object infoRequestParameters;

    // Getters and Setters
    public String getXboxToken() {
        return xboxToken;
    }

    public void setXboxToken(String xboxToken) {
        this.xboxToken = xboxToken;
    }

    public boolean isCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(boolean createAccount) {
        this.createAccount = createAccount;
    }

    public Object getCustomTags() {
        return customTags;
    }

    public void setCustomTags(Object customTags) {
        this.customTags = customTags;
    }

    public Object getInfoRequestParameters() {
        return infoRequestParameters;
    }

    public void setInfoRequestParameters(Object infoRequestParameters) {
        this.infoRequestParameters = infoRequestParameters;
    }

}

package com.models.PsnModel;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * URL and size for a PSN user’s avatar image.
 */
public class AvatarUrl {
    @JsonProperty("size")
    private int size;

    @JsonProperty("value")
    private String url;

    // getters & setters
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}

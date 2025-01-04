package com.models.NewsModel;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class News {
    private List <NewsResults> results;

    public List<NewsResults> getResults() {
        return results;
    }

    public void setResults(List<NewsResults> results) {
        this.results = results;
    }
}

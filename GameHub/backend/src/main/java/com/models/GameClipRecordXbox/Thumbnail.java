// package com.models.GameClipRecordXbox;
// import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// @JsonIgnoreProperties(ignoreUnknown = true)
// public class Thumbnail {
//     private String uri;

//     public String getUri() {
//         return uri;
//     }

//     public void setUri(String uri) {
//         this.uri = uri;
//     }
    
// }

package com.models.GameClipRecordXbox;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Thumbnail {
    private String uri;
    private int fileSize;
    private int width;
    private int height;
    
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public int getFileSize() {
        return fileSize;
    }
    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
    
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
}

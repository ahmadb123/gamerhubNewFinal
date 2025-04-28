package com.dto;

public class ReplyNewsDTO {
    private Long newsId;
    private String reply;
    private String backgroundImage;
    private String contentText;
    public ReplyNewsDTO() {
    }

    public ReplyNewsDTO(Long newsId, String reply, String backgroundImage, String contentText) {
        this.newsId = newsId;
        this.reply = reply;
        this.backgroundImage = backgroundImage;
        this.contentText = contentText;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getbackgroundImage() {
        return backgroundImage;
    }

    public void setbackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getcontentText() {
        return contentText;
    }

    public void setcontentText(String contentText) {
        this.contentText = contentText;
    }

}

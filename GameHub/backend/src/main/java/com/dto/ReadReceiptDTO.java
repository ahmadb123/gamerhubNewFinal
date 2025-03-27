package com.dto;

import com.models.ChatsAndDirectMessages.MessageStatus;

public class ReadReceiptDTO {
    private MessageStatus msgStatus;
    private Long sessionId;
    private String receiverUsername;
    private String timestamp;


    public ReadReceiptDTO(MessageStatus msgStatus, Long sessionId, String receiverUsername, String timestamp) {
        this.msgStatus = msgStatus;
        this.sessionId = sessionId;
        this.receiverUsername = receiverUsername;
        this.timestamp = timestamp;
    }

    public ReadReceiptDTO() {
    }

    public MessageStatus getType() {
        return msgStatus;
    }

    public void setType(MessageStatus msgStatus) {
        this.msgStatus = msgStatus;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

package com.dto;

import com.models.ChatsAndDirectMessages.MessageStatus;

public class ReadReceiptDTO {
    private String eventType; // "READ_RECEIPT"
    private MessageStatus msgStatus;
    private Long sessionId;
    private String receiverUsername;
    private String timestamp;
    private boolean active;

    public ReadReceiptDTO(MessageStatus msgStatus, Long sessionId, String receiverUsername, String timestamp, String eventType, boolean active) {
        this.msgStatus = msgStatus;
        this.sessionId = sessionId;
        this.receiverUsername = receiverUsername;
        this.timestamp = timestamp;
        this.eventType = eventType;
        this.active = active;
    }

    public ReadReceiptDTO() {
    }

    public MessageStatus getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(MessageStatus msgStatus) {
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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}

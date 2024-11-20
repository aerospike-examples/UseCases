package com.aerospike.usecases.rtb.model;

import java.time.LocalDateTime;

enum EventType {
    VIEW, CLICK, IMPRESSION, INCART, PURCHASE
}

public class ActivityEvent {
    private String id;
    private String pageUrl;
    private EventType eventType;
    private LocalDateTime timestamp;

    public ActivityEvent(String id, String pageUrl, EventType eventType) {
        this.id = id;
        this.pageUrl = pageUrl;
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public EventType getEventType() {
        return eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}

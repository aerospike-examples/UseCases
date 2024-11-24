package com.aerospike.usecases.rtb.model;

import java.util.Date;

import lombok.NoArgsConstructor;

enum EventType {
    VIEW, CLICK, IMPRESSION, INCART, PURCHASE
}

@NoArgsConstructor
public class ActivityEvent {
    private String id;
    private String pageUrl;
    private EventType eventType;
    private Date timestamp;

    public ActivityEvent(String id, String pageUrl, EventType eventType) {
        this.id = id;
        this.pageUrl = pageUrl;
        this.eventType = eventType;
        this.timestamp = Date.now();
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

    public Date getTimestamp() {
        return timestamp;
    }

}

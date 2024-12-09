package com.aerospike.usecases.model;

import java.util.Date;

import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.Map;

enum EventType {
    VIEW, CLICK, IMPRESSION, INCART, PURCHASE
}

/**
 * Represents an activity event that captures user activity on a web page or
 * app. Part of the UserProfile.
 * 
 * <p>
 * This class is annotated with {@code @AerospikeRecord} to indicate that it is
 * an Aerospike record. It includes fields for the event ID, page URL, event
 * type, and timestamp.
 * </p>
 * 
 * <p>
 * Constructors are provided to create an instance with or without a timestamp.
 * If the timestamp is not provided, the current date and time will be used.
 * </p>
 * 
 * <p>
 * Getters are available for all fields to retrieve their values.
 * </p>
 * 
 * <p>
 * Annotations:
 * </p>
 * <ul>
 * <li>{@code @NoArgsConstructor} - Generates a no-argument constructor.</li>
 * <li>{@code @AllArgsConstructor} - Generates a constructor with arguments for
 * all fields.</li>
 * </ul>
 * 
 * <p>
 * Fields:
 * </p>
 * <ul>
 * <li>{@code id} - The unique identifier for the event.</li>
 * <li>{@code pageUrl} - The URL of the page where the event occurred.</li>
 * <li>{@code eventType} - The type of event (e.g., click, view).</li>
 * <li>{@code timestamp} - The timestamp when the event occurred.</li>
 * </ul>
 */
@AerospikeRecord
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
        this.timestamp = new Date();
    }

    public ActivityEvent(String id, String pageUrl, EventType eventType, Date timestamp) {
        this.id = id;
        this.pageUrl = pageUrl;
        this.eventType = eventType;
        this.timestamp = timestamp;
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

    public Map<String, Object> asMap() {
        // for native storage manager
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("pageUrl", pageUrl);
        map.put("eventType", eventType);
        map.put("timestamp", timestamp);
        return map;
    }

    public static ActivityEvent fromMap(Map<String, Object> map) {
        // for native storage manager
        return new ActivityEvent((String) map.get("id"), (String) map.get("pageUrl"), (EventType) map.get("eventType"),
                (Date) map.get("timestamp"));
    }

}

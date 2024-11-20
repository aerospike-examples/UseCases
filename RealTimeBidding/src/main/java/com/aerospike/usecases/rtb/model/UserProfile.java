package com.aerospike.usecases.rtb.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.aerospike.mapper.annotations.AerospikeEmbed;
import com.aerospike.mapper.annotations.AerospikeKey;
import com.aerospike.mapper.annotations.AerospikeRecord;
import com.aerospike.mapper.annotations.AerospikeEmbed.EmbedType;

import lombok.AllArgsConstructor;
import lombok.Data;

@AerospikeRecord(namespace = "test", set = "profiles")
@AllArgsConstructor
@Data
public class UserProfile {
    @AerospikeKey
    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // user demographics
    @AerospikeEmbed(type = EmbedType.LIST, elementType = EmbedType.MAP)
    private Demographics demographics;
    // simplified interests for the sake of the example
    @AerospikeEmbed(type = EmbedType.LIST)
    private List<String> interests;
    // user location
    private Location location;
    // devices are stored as a list of device
    @AerospikeEmbed(type = EmbedType.LIST, elementType = EmbedType.MAP)
    private List<Device> devices;
    // simplified activity history is a list of URLs
    @AerospikeEmbed(type = EmbedType.LIST, elementType = EmbedType.MAP)
    private List<ActivityEvent> activity;
    // simplified purchase history is a list of purchases
    @AerospikeEmbed(type = EmbedType.LIST, elementType = EmbedType.MAP)
    private List<Purchase> purchases;
    // eligible lineitem IDs for this user
    @AerospikeEmbed(type = EmbedType.LIST)
    private List<String> lineitemIds;

    public UserProfile(String id, Demographics demographics, Location location) {

        this.id = id;
        this.demographics = demographics;
        this.location = location;
        this.interests = new ArrayList<String>();
        this.devices = new ArrayList<Device>();
        this.activity = new ArrayList<ActivityEvent>();
        this.purchases = new ArrayList<Purchase>();
        this.lineitemIds = new ArrayList<String>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void addLineitem(String lineitemId) {
        this.lineitemIds.add(lineitemId);
    }

    public void setLineitems(List<String> lineitemIds) {
        this.lineitemIds = lineitemIds;
    }

    public void addDevice(Device device) {
        this.devices.add(device);
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public void addActivityEvent(ActivityEvent activityEvent) {
        this.activity.add(activityEvent);
    }

    public void addPurchase(Purchase purchase) {
        this.purchases.add(purchase);
    }

    public void addInterest(String interest) {
        this.interests.add(interest);
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

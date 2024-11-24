package com.aerospike.usecases.rtb.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.aerospike.mapper.annotations.AerospikeEmbed;
import com.aerospike.mapper.annotations.AerospikeKey;
import com.aerospike.mapper.annotations.AerospikeRecord;
import com.aerospike.mapper.annotations.AerospikeEmbed.EmbedType;

import lombok.AllArgsConstructor;
import lombok.Data;

@AerospikeRecord(namespace = "rtb", set = "profiles")
@AllArgsConstructor
@Data
public class UserProfile {
    @AerospikeKey
    private String id;
    private Date createdAt;
    private Date updatedAt;
    // user demographics
    @AerospikeEmbed(type = EmbedType.MAP)
    private Demographics demographics;
    // simplified interests for the sake of the example
    @AerospikeEmbed(type = EmbedType.LIST)
    private List<String> interests;
    // user location
    @AerospikeEmbed(type = EmbedType.MAP)
    private Location location;
    // simplified activity history is a list of URLs
    @AerospikeEmbed(type = EmbedType.LIST, elementType = EmbedType.MAP)
    private List<ActivityEvent> activity;
    // simplified purchase history is a list of purchases
    @AerospikeEmbed(type = EmbedType.LIST, elementType = EmbedType.MAP)
    private List<Purchase> purchases;
    // eligible lineitem IDs for this user
    @AerospikeEmbed(type = EmbedType.LIST, elementType = EmbedType.MAP)
    private List<Lineitem> lineitems;

    public UserProfile() {
        this.interests = new ArrayList<String>();
        this.activity = new ArrayList<ActivityEvent>();
        this.purchases = new ArrayList<Purchase>();
        this.lineitems = new ArrayList<Lineitem>();
    }

    public UserProfile(String id, Demographics demographics, Location location) {
        this();
        this.id = id;
        this.demographics = demographics;
        this.location = location;

        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public void addLineitem(Lineitem lineitem) {
        this.lineitems.add(lineitem);
    }

    public void setLineitems(List<Lineitem> lineitems) {
        this.lineitems = lineitems;
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
        this.updatedAt = new Date();
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

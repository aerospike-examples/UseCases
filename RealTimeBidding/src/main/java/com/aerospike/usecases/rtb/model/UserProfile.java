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

/**
 * Represents a user profile in the real-time bidding system. This class is
 * annotated with AerospikeRecord to map it to an Aerospike database record. It
 * contains various user-related information such as demographics, interests,
 * location, activity history, and purchase history.
 * 
 * Annotations: - @AerospikeRecord: Specifies the Aerospike namespace and set
 * for the record. - @AllArgsConstructor: Generates a constructor with one
 * parameter for each field in the class. - @Data: Generates getters, setters,
 * toString, equals, and hashCode methods. - @AerospikeKey: Marks the field as
 * the primary key for the Aerospike record. - @AerospikeEmbed: Specifies how to
 * embed complex types in the Aerospike record.
 * 
 * Fields: - id: The unique identifier for the user profile. - createdAt: The
 * date and time when the profile was created. - updatedAt: The date and time
 * when the profile was last updated. - demographics: The demographic
 * information of the user. - interests: A list of the user's interests. -
 * location: The location information of the user. - activity: A list of the
 * user's activity events. - purchases: A list of the user's purchase history. -
 * lineitems: A list of eligible lineitem IDs for the user.
 * 
 * Constructors: - UserProfile(): Initializes the lists for interests, activity,
 * purchases, and lineitems. - UserProfile(String id, Demographics demographics,
 * Location location): Initializes the profile with the given id, demographics,
 * and location, and sets the createdAt and updatedAt fields to the current date
 * and time.
 * 
 * Methods: - addLineitem(Lineitem lineitem): Adds a lineitem ID to the list of
 * lineitems. - setLineitems(List<String> lineitems): Sets the list of
 * lineitems. - addActivityEvent(ActivityEvent activityEvent): Adds an activity
 * event to the list of activity events. - addPurchase(Purchase purchase): Adds
 * a purchase to the list of purchases. - addInterest(String interest): Adds an
 * interest to the list of interests. - setInterests(List<String> interests):
 * Sets the list of interests. - touch(): Updates the updatedAt field to the
 * current date and time. - setLocation(Location location): Sets the location
 * information. - toString(): Returns a string representation of the user
 * profile.
 */
@AerospikeRecord(namespace = "${rtb.namespace:rtb}", set = "profiles")
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
    @AerospikeEmbed(type = EmbedType.LIST)
    private List<String> lineitemIds;

    public UserProfile() {
        this.interests = new ArrayList<String>();
        this.activity = new ArrayList<ActivityEvent>();
        this.purchases = new ArrayList<Purchase>();
        this.lineitemIds = new ArrayList<String>();
    }

    public UserProfile(String id, Demographics demographics, List<String> interests, Location location) {
        this();
        this.id = id;
        this.demographics = demographics;
        this.location = location;
        this.interests = interests;

        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public void addLineitem(Lineitem lineitem) {
        this.lineitemIds.add(lineitem.getId());
    }

    public void setLineitemIds(List<String> lineitemids) {
        this.lineitemIds = lineitemids;
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

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Demographics getDemographics() {
        return demographics;
    }

    public List<String> getInterests() {
        return interests;
    }

    public Location getLocation() {
        return location;
    }

    public List<ActivityEvent> getActivity() {
        return activity;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public List<String> getLineitemIds() {
        return lineitemIds;
    }

    @Override
    public String toString() {
        return "UserProfile{" + "id='" + id + '\'' + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
                + ", demographics=" + demographics + ", interests=" + interests + ", location=" + location
                + ", activity=" + activity + ", purchases=" + purchases + ", lineitems=" + lineitemIds + '}';
    }
}

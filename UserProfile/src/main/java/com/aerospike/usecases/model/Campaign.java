package com.aerospike.usecases.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.aerospike.mapper.annotations.AerospikeEmbed;
import com.aerospike.mapper.annotations.AerospikeEmbed.EmbedType;
import com.aerospike.mapper.annotations.AerospikeKey;
import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.AllArgsConstructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Buy Side Campaign in the Real-Time Bidding system.
 * 
 * <p>
 * This class is annotated with AerospikeRecord to map it to the Aerospike
 * database. It contains various fields related to a campaign such as id, name,
 * description, advertiserId, lineitemIds, startDate, endDate, budget,
 * budgetSpent, and status.
 * </p>
 * 
 * <p>
 * It provides constructors for creating a Campaign instance, getter methods to
 * access the fields, and setter methods to modify the fields. Additionally, it
 * includes methods to add line items and a toString method for representing the
 * Campaign object as a string.
 * </p>
 * 
 * <p>
 * Fields:
 * </p>
 * <ul>
 * <li>{@code id} - The unique identifier for the campaign.</li>
 * <li>{@code name} - The name of the campaign.</li>
 * <li>{@code description} - A brief description of the campaign.</li>
 * <li>{@code advertiserId} - The identifier of the advertiser associated with
 * the campaign.</li>
 * <li>{@code lineitemIds} - A list of line item identifiers associated with the
 * campaign.</li>
 * <li>{@code startDate} - The start date of the campaign.</li>
 * <li>{@code endDate} - The end date of the campaign.</li>
 * <li>{@code budget} - The total budget allocated for the campaign.</li>
 * <li>{@code budgetSpent} - The amount of budget spent so far.</li>
 * <li>{@code status} - The current status of the campaign.</li>
 * </ul>
 * 
 * <p>
 * Methods:
 * </p>
 * <ul>
 * <li>{@code getId()} - Returns the campaign id.</li>
 * <li>{@code getName()} - Returns the campaign name.</li>
 * <li>{@code getDescription()} - Returns the campaign description.</li>
 * <li>{@code getAdvertiserId()} - Returns the advertiser id.</li>
 * <li>{@code getLineitemIds()} - Returns the list of line item ids.</li>
 * <li>{@code getStartDate()} - Returns the start date of the campaign.</li>
 * <li>{@code getEndDate()} - Returns the end date of the campaign.</li>
 * <li>{@code getBudget()} - Returns the campaign budget.</li>
 * <li>{@code getBudgetSpent()} - Returns the amount of budget spent.</li>
 * <li>{@code getStatus()} - Returns the campaign status.</li>
 * <li>{@code addLineitem(Lineitem lineitem)} - Adds a line item to the
 * campaign.</li>
 * <li>{@code addLineitem(String id)} - Adds a line item id to the
 * campaign.</li>
 * <li>{@code setStatus(CampaignStatus status)} - Sets the campaign status.</li>
 * <li>{@code setEndDate(Date endDate)} - Sets the end date of the
 * campaign.</li>
 * <li>{@code setBudgetSpent(int budgetSpent)} - Sets the amount of budget
 * spent.</li>
 * <li>{@code setDescription(String description)} - Sets the campaign
 * description.</li>
 * <li>{@code setLineitems(List<Lineitem> lineitems)} - Sets the list of line
 * items.</li>
 */
@AerospikeRecord(namespace = "${rtb.namespace:rtb}", set = "campaigns")
@AllArgsConstructor

public class Campaign {
    @AerospikeKey
    String id;
    String name;
    String description;
    String advertiserId;
    @AerospikeEmbed(type = EmbedType.LIST)
    List<String> lineitemIds;
    Date startDate;
    Date endDate;
    int budget;
    int budgetSpent;
    CampaignStatus status;

    public Campaign() {
        this.lineitemIds = new ArrayList<String>();
    }

    public Campaign(String id, String name, String advertiserId, Date startDate, Date endDate, int budget) {
        this();
        this.id = id;
        this.name = name;
        this.advertiserId = advertiserId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.budgetSpent = 0;
        this.status = CampaignStatus.DRAFT;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAdvertiserId() {
        return advertiserId;
    }

    public List<String> getLineitemIds() {
        return lineitemIds;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public float getBudget() {
        return budget;
    }

    public float getBudgetSpent() {
        return budgetSpent;
    }

    public CampaignStatus getStatus() {
        return status;
    }

    public void addLineitem(Lineitem lineitem) {
        this.lineitemIds.add(lineitem.getId());
    }

    public void addLineitem(String id) {
        this.lineitemIds.add(id);
    }

    public void setStatus(CampaignStatus status) {
        this.status = status;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setBudgetSpent(int budgetSpent) {
        this.budgetSpent = budgetSpent;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLineitems(List<Lineitem> lineitems) {
        List<String> ids = new ArrayList<String>();
        for (Lineitem lineitem : lineitems) {
            ids.add(lineitem.getId());
        }
        this.setLineitemIds(ids);
    }

    public void setLineitemIds(List<String> ids) {
        this.lineitemIds = ids;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    @Override
    public String toString() {
        return "Campaign{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", description='" + description + '\''
                + ", advertiserId='" + advertiserId + '\'' + ", lineitemIds=" + lineitemIds + ", startDate=" + startDate
                + ", endDate=" + endDate + ", budget=" + budget + ", budgetSpent=" + budgetSpent + ", status=" + status
                + '}';
    }

    public Map<String, Object> asMap() {
        // for native storage manager
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("description", description);
        map.put("advertiserId", advertiserId);
        map.put("lineitemIds", lineitemIds);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("budget", budget);
        map.put("budgetSpent", budgetSpent);
        map.put("status", status);
        return map;
    }

    @SuppressWarnings("unchecked")
    public static Campaign fromMap(Map<?, ?> map) {
        // for native storage manager
        Campaign campaign = new Campaign();
        campaign.id = (String) map.get("id");
        campaign.name = (String) map.get("name");
        campaign.description = (String) map.get("description");
        campaign.advertiserId = (String) map.get("advertiserId");
        campaign.lineitemIds = (List<String>) map.get("lineitemIds");
        campaign.startDate = (Date) map.get("startDate");
        campaign.endDate = (Date) map.get("endDate");
        campaign.budget = (int) map.get("budget");
        campaign.budgetSpent = (int) map.get("budgetSpent");
        campaign.status = (CampaignStatus) map.get("status");
        return campaign;
    }

}

package com.aerospike.usecases.rtb.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.aerospike.mapper.annotations.AerospikeEmbed;
import com.aerospike.mapper.annotations.AerospikeEmbed.EmbedType;
import com.aerospike.mapper.annotations.AerospikeKey;
import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.AllArgsConstructor;

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

}

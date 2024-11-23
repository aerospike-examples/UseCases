package com.aerospike.usecases.rtb.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.aerospike.mapper.annotations.AerospikeEmbed;
import com.aerospike.mapper.annotations.AerospikeEmbed.EmbedType;
import com.aerospike.mapper.annotations.AerospikeKey;
import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AerospikeRecord(namespace = "rtb", set = "campaigns")
@AllArgsConstructor

public class Campaign {
    @AerospikeKey
    String id;
    String name;
    String description;
    String advertiserId;
    @AerospikeEmbed(type = EmbedType.LIST)
    List<String> lineitemIds;
    LocalDateTime startDate;
    LocalDateTime endDate;
    int budget;
    int budgetSpent;
    CampaignStatus status;

    public Campaign() {
        this.lineitemIds = new ArrayList<String>();
    }

    public Campaign(String id, String name, String advertiserId, LocalDateTime startDate, LocalDateTime endDate,
            int budget) {
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
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

    public void setEndDate(LocalDateTime endDate) {
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

}

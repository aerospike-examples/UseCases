package com.aerospike.usecases.rtb.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.units.qual.N;

import com.aerospike.mapper.annotations.AerospikeEmbed;
import com.aerospike.mapper.annotations.AerospikeEmbed.EmbedType;
import com.aerospike.mapper.annotations.AerospikeKey;
import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.AllArgsConstructor;
import lombok.Data;

@AerospikeRecord(namespace = "rtb", set = "lineitems")
@AllArgsConstructor
public class Lineitem {
    @AerospikeKey
    private String id;
    // The campaign id that this lineitem belongs to
    private String campaignId;
    // The name of the lineitem
    private String name;
    // The start and end dates of the lineitem
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    // The budget of the lineitem
    private int budget;
    // The audience that this lineitem is targeting
    @AerospikeEmbed(type = EmbedType.MAP)
    private Audience audience;
    // The status of the lineitem
    private LineitemStatus status;
    // The list of creatives that this lineitem is using
    @AerospikeEmbed(type = EmbedType.LIST)
    private List<String> creativeIds;

    public Lineitem() {
        this.creativeIds = new ArrayList<String>();
    }

    public Lineitem(String id, String campaignId, String name, LocalDateTime startDate, LocalDateTime endDate,
            int budget) {
        this();
        this.id = id;
        this.campaignId = campaignId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.status = LineitemStatus.DRAFT;
    }

    public String getId() {
        return id;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public int getBudget() {
        return budget;
    }

    public Audience getAudience() {
        return audience;
    }

    public LineitemStatus getStatus() {
        return status;
    }

    public List<String> getCreatives() {
        return creativeIds;
    }

    public void addCreative(String creativeId) {
        this.creativeIds.add(creativeId);
    }

    public void addCreative(Creative creative) {
        this.creativeIds.add(creative.getId());
    }

    public void setStatus(LineitemStatus status) {
        this.status = status;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

}

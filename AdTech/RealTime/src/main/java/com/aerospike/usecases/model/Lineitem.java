
package com.aerospike.usecases.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.aerospike.mapper.annotations.AerospikeEmbed;
import com.aerospike.mapper.annotations.AerospikeEmbed.EmbedType;
import com.aerospike.mapper.annotations.AerospikeKey;
import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.AllArgsConstructor;

/**
 * Represents a Lineitem which is a portion of a Campaign and represents an
 * execution plan. This class is annotated to be stored in an Aerospike
 * database.
 * 
 * @AerospikeRecord(namespace = "${rtb.namespace:rtb}", set = "lineitems")
 * 
 * @author Peter Milne
 */
@AerospikeRecord(namespace = "${rtb.namespace:rtb}", set = "lineitems")
public class Lineitem {
    @AerospikeKey
    private String id;
    // The campaign id that this lineitem belongs to
    private String campaignId;
    // The name of the lineitem
    private String name;
    // The start and end dates of the lineitem
    private Date startDate;
    private Date endDate;
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

    public Lineitem(String id, String campaignId, String name, Date startDate, Date endDate, int budget) {
        this();
        this.id = id;
        this.campaignId = campaignId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.status = LineitemStatus.DRAFT;
    }

    public Lineitem(String id, String campaignId, String name, Date startDate, Date endDate, int budget,
            Audience audience, LineitemStatus status, List<String> creatives) {
        this(id, campaignId, name, startDate, endDate, budget);
        this.status = status;
        this.audience = audience;
        this.creativeIds = creatives;

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

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
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

    @Override
    public String toString() {
        return "Lineitem{" + "id='" + id + '\'' + ", campaignId='" + campaignId + '\'' + ", name='" + name + '\''
                + ", startDate=" + startDate + ", endDate=" + endDate + ", budget=" + budget + ", audience=" + audience
                + ", status=" + status + ", creativeIds=" + creativeIds + '}';
    }

}

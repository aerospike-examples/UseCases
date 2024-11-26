package com.aerospike.usecases.rtb.model;

import java.util.ArrayList;
import java.util.List;

import com.aerospike.mapper.annotations.AerospikeEmbed;
import com.aerospike.mapper.annotations.AerospikeEmbed.EmbedType;
import com.aerospike.mapper.annotations.AerospikeKey;
import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.AllArgsConstructor;

@AerospikeRecord(namespace = "${rtb.namespace:rtb}", set = "audiences")
@AllArgsConstructor
public class Audience {
    @AerospikeKey
    private String id;
    // The name of the audience
    private String name;
    // The list of segment ids that make up the audience
    @AerospikeEmbed(type = EmbedType.LIST)
    private List<String> segmentIds;

    public Audience() {
        this.segmentIds = new ArrayList<String>();
    }

    public Audience(String id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getSegmentIds() {
        return segmentIds;
    }

    public void addSegment(Segment segment) {
        this.segmentIds.add(segment.getId());
    }

    public void addSegment(String id) {
        this.segmentIds.add(id);
    }

    public void setSegmentIds(List<String> segmentIds) {
        this.segmentIds = segmentIds;
    }
}

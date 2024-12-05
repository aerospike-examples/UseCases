package com.aerospike.usecases.rtb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aerospike.mapper.annotations.AerospikeEmbed;
import com.aerospike.mapper.annotations.AerospikeEmbed.EmbedType;
import com.aerospike.mapper.annotations.AerospikeKey;
import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.AllArgsConstructor;

/**
 * Represents an audience which is a collection of segments to target in a
 * Lineitem.
 * 
 * <p>
 * This class is annotated with AerospikeRecord to specify the namespace and set
 * for Aerospike database. It contains an id, name, and a list of segment ids
 * that make up the audience.
 * </p>
 * 
 * <p>
 * It provides methods to get the id, name, and segment ids, as well as methods
 * to add segments and set the segment ids.
 * </p>
 * 
 * <p>
 * Constructors are provided to initialize the audience with or without initial
 * values.
 * </p>
 * 
 * <p>
 * The toString method is overridden to provide a string representation of the
 * audience object.
 * </p>
 * 
 * @see Segment
 */
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

    @Override
    public String toString() {
        return "Audience{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", segmentIds=" + segmentIds + '}';
    }

    @SuppressWarnings("unchecked")
    public static Audience fromMap(Map<?, ?> map) {
        Audience audience = new Audience();
        audience.id = (String) map.get("id");
        audience.name = (String) map.get("name");
        audience.segmentIds = (List<String>) map.get("segmentIds");
        return audience;
    }

    public Map<String, Object> asMap() {
        // for native storage manager
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("segmentIds", segmentIds);
        return map;
    }
}

package com.aerospike.usecases.rtb.model;

import java.util.Date;
import java.util.List;

import com.aerospike.mapper.annotations.AerospikeEmbed;
import com.aerospike.mapper.annotations.AerospikeEmbed.EmbedType;
import com.aerospike.mapper.annotations.AerospikeKey;
import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.AllArgsConstructor;
import lombok.Data;

enum DeviceType {
    MOBILE, DESKTOP, TABLET
}

@AerospikeRecord(namespace = "${rtb.namespace:rtb}", set = "segments")
@AllArgsConstructor
@Data
public class Segment {
    @AerospikeKey
    private String id;
    private String name;
    private String description;
    private int size;
    private Date creationDate;
    private Date lastUpdated;
    @AerospikeEmbed(type = EmbedType.MAP)
    private DeviceType device;
    @AerospikeEmbed(type = EmbedType.MAP)
    private Demographics demographics;
    // simplified interests for the sake of the example
    @AerospikeEmbed(type = EmbedType.LIST)
    private List<String> interests;

    public Segment(String id, String name, String description, int size, DeviceType device, Demographics demographics) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.size = size;
        this.device = device;
        this.demographics = demographics;
        this.creationDate = new Date();
        this.lastUpdated = new Date();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void touch() {
        this.lastUpdated = new Date();
    }

    public List<String> getInterests() {
        return interests;
    }

    public Demographics getDemographics() {
        return demographics;
    }

    public DeviceType getDevice() {
        return device;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getId() {
        return id;
    }

    public void addInterest(String interest) {
        this.interests.add(interest);
    }

}

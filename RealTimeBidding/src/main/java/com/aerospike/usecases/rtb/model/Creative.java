package com.aerospike.usecases.rtb.model;

import com.aerospike.mapper.annotations.AerospikeEmbed;
import com.aerospike.mapper.annotations.AerospikeEmbed.EmbedType;
import com.aerospike.mapper.annotations.AerospikeKey;
import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.Data;

@AerospikeRecord(namespace = "rtb", set = "creatives")
@Data
public class Creative {
    @AerospikeKey
    private String id;
    private String name;
    private String url;
    private String format;
    @AerospikeEmbed(type = EmbedType.MAP)
    private Size size;
    private String advertiserId;
    private String lineitemId;
    private String type;

    public Creative(String id, String name, String url, String format, Size size, String advertiserId,
            String lineitemId, String type) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.format = format;
        this.size = size;
        this.advertiserId = advertiserId;
        this.lineitemId = lineitemId;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getFormat() {
        return format;
    }

    public Size getSize() {
        return size;
    }

    public String getAdvertiserId() {
        return advertiserId;
    }

    public String getLineitemId() {
        return lineitemId;
    }

    public String getType() {
        return type;
    }

}

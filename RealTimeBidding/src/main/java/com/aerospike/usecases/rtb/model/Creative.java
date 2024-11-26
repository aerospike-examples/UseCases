package com.aerospike.usecases.rtb.model;

import java.util.ArrayList;
import java.util.List;

import com.aerospike.mapper.annotations.AerospikeEmbed;
import com.aerospike.mapper.annotations.AerospikeEmbed.EmbedType;
import com.aerospike.mapper.annotations.AerospikeKey;
import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.AllArgsConstructor;

@AerospikeRecord(namespace = "${rtb.namespace:rtb}", set = "creatives")
@AllArgsConstructor
public class Creative {
    @AerospikeKey
    private String id;
    private String name;
    private String url;
    @AerospikeEmbed(type = EmbedType.LIST, elementType = EmbedType.MAP)
    private List<Size> sizes;
    private String advertiserId;
    private String lineitemId;
    private String type;

    public Creative() {
        this.sizes = new ArrayList<Size>();
    }

    public Creative(String id, String name, String url, String advertiserId, String lineitemId, String type) {
        this();
        this.id = id;
        this.name = name;
        this.url = url;
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

    public List<Size> getSizes() {
        return sizes;
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

    public void addSize(Size size) {
        this.sizes.add(size);
    }

    public void setSizes(List<Size> sizes) {
        this.sizes = sizes;
    }

    @Override
    public String toString() {
        return "Creative{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", url='" + url + '\'' + ", sizes=" + sizes
                + ", advertiserId='" + advertiserId + '\'' + ", lineitemId='" + lineitemId + '\'' + ", type='" + type
                + '\'' + '}';
    }

}

package com.aerospike.usecases.rtb.model;

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
 * Represents a creative entity in the real-time bidding system. A creative can
 * be a banner ad, video, or other types of electronic advertisements.
 * 
 * <p>
 * This class is annotated with AerospikeRecord to map it to an Aerospike
 * database. It contains various attributes related to the creative such as id,
 * name, url, sizes, advertiserId, lineitemId, and type.
 * </p>
 * 
 * <p>
 * Attributes:
 * </p>
 * <ul>
 * <li>{@code id} - The unique identifier for the creative.</li>
 * <li>{@code name} - The name of the creative.</li>
 * <li>{@code url} - The URL associated with the creative.</li>
 * <li>{@code sizes} - A list of sizes for the creative, embedded as a list of
 * maps.</li>
 * <li>{@code advertiserId} - The identifier for the advertiser associated with
 * the creative.</li>
 * <li>{@code lineitemId} - The identifier for the line item associated with the
 * creative.</li>
 * <li>{@code type} - The type of the creative (e.g., banner, video).</li>
 * </ul>
 * 
 * <p>
 * Methods:
 * </p>
 * <ul>
 * <li>{@code getId()} - Returns the id of the creative.</li>
 * <li>{@code getName()} - Returns the name of the creative.</li>
 * <li>{@code getUrl()} - Returns the URL of the creative.</li>
 * <li>{@code getSizes()} - Returns the list of sizes for the creative.</li>
 * <li>{@code getAdvertiserId()} - Returns the advertiser id of the
 * creative.</li>
 * <li>{@code getLineitemId()} - Returns the line item id of the creative.</li>
 * <li>{@code getType()} - Returns the type of the creative.</li>
 * <li>{@code addSize(Size size)} - Adds a size to the list of sizes for the
 * creative.</li>
 * <li>{@code setSizes(List<Size> sizes)} - Sets the list of sizes for the
 * creative.</li>
 * <li>{@code toString()} - Returns a string representation of the
 * creative.</li>
 * </ul>
 * 
 * <p>
 * Constructors:
 * </p>
 * <ul>
 * <li>{@code Creative()} - Default constructor initializing the sizes
 * list.</li>
 * <li>{@code Creative(String id, String name, String url, String advertiserId, String lineitemId, String type)}
 * - Constructor initializing all attributes and the sizes list.</li>
 * </ul>
 */
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

    public Map<String, Object> asMap() {
        // for native storage manager
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("url", url);
        map.put("sizes", sizes);
        map.put("advertiserId", advertiserId);
        map.put("lineitemId", lineitemId);
        map.put("type", type);
        return map;
    }

    @SuppressWarnings("unchecked")
    public static Creative fromMap(Map<String, Object> map) {
        // for native storage manager
        Creative creative = new Creative();
        creative.id = (String) map.get("id");
        creative.name = (String) map.get("name");
        creative.url = (String) map.get("url");
        creative.sizes = (List<Size>) map.get("sizes");
        creative.advertiserId = (String) map.get("advertiserId");
        creative.lineitemId = (String) map.get("lineitemId");
        creative.type = (String) map.get("type");
        return creative;
    }

}

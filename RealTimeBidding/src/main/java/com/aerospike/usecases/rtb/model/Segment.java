package com.aerospike.usecases.rtb.model;

import java.util.Date;
import java.util.List;

import com.aerospike.mapper.annotations.AerospikeEmbed;
import com.aerospike.mapper.annotations.AerospikeEmbed.EmbedType;
import com.aerospike.mapper.annotations.AerospikeKey;
import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

enum DeviceType {
    MOBILE, DESKTOP, TABLET
}

/**
 * Represents a segment in an Audience of the real-time bidding system.
 * 
 * <p>
 * This class is annotated with AerospikeRecord to map it to an Aerospike
 * database record. It contains various fields such as id, name, description,
 * size, creationDate, lastUpdated, device, demographics, and interests. The
 * class provides constructors, getters, setters, and utility methods to manage
 * the segment data.
 * </p>
 * 
 * <p>
 * Fields:
 * </p>
 * <ul>
 * <li>{@code id} - The unique identifier for the segment.</li>
 * <li>{@code name} - The name of the segment.</li>
 * <li>{@code description} - A brief description of the segment.</li>
 * <li>{@code size} - The size of the segment.</li>
 * <li>{@code creationDate} - The date when the segment was created.</li>
 * <li>{@code lastUpdated} - The date when the segment was last updated.</li>
 * <li>{@code device} - The device type associated with the segment, embedded as
 * a map.</li>
 * <li>{@code demographics} - The demographics associated with the segment,
 * embedded as a map.</li>
 * <li>{@code interests} - A list of interests associated with the segment,
 * embedded as a list.</li>
 * </ul>
 * 
 * <p>
 * Methods:
 * </p>
 * <ul>
 * <li>{@code Segment(String id, String name, String description, int size, DeviceType device, Demographics demographics)}
 * - Constructor to initialize a segment with specified values.</li>
 * <li>{@code setDescription(String description)} - Sets the description of the
 * segment.</li>
 * <li>{@code setName(String name)} - Sets the name of the segment.</li>
 * <li>{@code setSize(int size)} - Sets the size of the segment.</li>
 * <li>{@code touch()} - Updates the lastUpdated field to the current date.</li>
 * <li>{@code getInterests()} - Returns the list of interests associated with
 * the segment.</li>
 * <li>{@code getDemographics()} - Returns the demographics associated with the
 * segment.</li>
 * <li>{@code getDevice()} - Returns the device type associated with the
 * segment.</li>
 * <li>{@code getDescription()} - Returns the description of the segment.</li>
 * <li>{@code getName()} - Returns the name of the segment.</li>
 * <li>{@code getSize()} - Returns the size of the segment.</li>
 * <li>{@code getLastUpdated()} - Returns the date when the segment was last
 * updated.</li>
 * <li>{@code getCreationDate()} - Returns the date when the segment was
 * created.</li>
 * <li>{@code getId()} - Returns the unique identifier of the segment.</li>
 * <li>{@code addInterest(String interest)} - Adds an interest to the list of
 * interests associated with the segment.</li>
 * <li>{@code toString()} - Returns a string representation of the segment.</li>
 * </ul>
 */
@AerospikeRecord(namespace = "${rtb.namespace:rtb}", set = "segments")
@AllArgsConstructor
@NoArgsConstructor
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

    @Override
    public String toString() {
        return "Segment{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", description='" + description + '\''
                + ", size=" + size + ", creationDate=" + creationDate + ", lastUpdated=" + lastUpdated + ", device="
                + device + ", demographics=" + demographics + ", interests=" + interests + '}';
    }

}

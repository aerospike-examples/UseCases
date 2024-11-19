package com.aerospike.usecases.rtb.model;

import java.util.ArrayList;
import java.util.List;

import com.aerospike.mapper.annotations.AerospikeEmbed;
import com.aerospike.mapper.annotations.AerospikeKey;
import com.aerospike.mapper.annotations.AerospikeRecord;
import com.aerospike.mapper.annotations.AerospikeEmbed.EmbedType;

import lombok.AllArgsConstructor;
import lombok.Data;

@AerospikeRecord(namespace = "test", set = "profiles")
@AllArgsConstructor
@Data
public class UserProfile {
    @AerospikeKey
    private String id;
    @AerospikeEmbed(type = EmbedType.MAP, elementType = EmbedType.LIST)
    private List<Lineitem> lineitems;

    public UserProfile() {
        this.lineitems = new ArrayList<Lineitem>();
    }

    public UserProfile(String id) {
        this();
        this.id = id;
    }

    /**
     * In production usage, token/cookie/device ids are likely to be Strings or
     * UUIDs or similar. Since the devices are going to be generated, it is easier
     * to use a numeric value to generate/fetch a device than a string. To mirror
     * realism though, Strings will be stored as the key. This function trivially
     * turns a <code>long</code> into a UUID-style string;
     * 
     * @param id - The numeric value of the identifier
     * @return A string value that is unique to the identifier
     */
    public static String idToString(long id) {
        return String.format("73dacfc7-cd3a-%04x-8c43-%012x", id ^ 0x2621, id);
    }
}

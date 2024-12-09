package com.aerospike.usecases.model;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.aerospike.mapper.annotations.AerospikeKey;
import com.aerospike.mapper.annotations.AerospikeOrdinal;
import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Deprecated
@Data
@NoArgsConstructor
@AerospikeRecord(namespace = "${rtb.namespace:rtb}", set = "segments")
public class SegmentInstance {
    @AerospikeKey
    private long segmentId;

    // Force the expiry date to be first in the list.
    @AerospikeOrdinal(value = 1)
    private Date expiry;
    private long flags;
    private String partnerId;

    public SegmentInstance(long segmentId, String partnerId, long flags, int daysToKeep) {
        long now = new Date().getTime();
        Date expiry = new Date(now + TimeUnit.DAYS.toMillis(daysToKeep));
        this.expiry = expiry;
        this.segmentId = segmentId;
        this.partnerId = partnerId;
        this.flags = flags;
    }

    public SegmentInstance(long segmentId, Date expiry, long flags, String partnerId) {
        this.expiry = expiry;
        this.segmentId = segmentId;
        this.partnerId = partnerId;
        this.flags = flags;
    }

    public long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(long segmentId) {
        this.segmentId = segmentId;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public long getFlags() {
        return flags;
    }

    public void setFlags(long flags) {
        this.flags = flags;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }
}

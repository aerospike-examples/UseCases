package com.aerospike.usecases.rtb.model;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.aerospike.mapper.annotations.AerospikeKey;
import com.aerospike.mapper.annotations.AerospikeOrdinal;
import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@AerospikeRecord(namespace = "test", set = "segments")
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
}

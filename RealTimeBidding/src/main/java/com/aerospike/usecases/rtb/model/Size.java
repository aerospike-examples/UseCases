package com.aerospike.usecases.rtb.model;

import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Represents the size of a banner with width and height dimensions. This class
 * is used in the Real-Time Bidding use case for Aerospike.
 * 
 * <p>
 * The {@code Size} class provides methods to retrieve the width and height of
 * the banner, and overrides the {@code toString} method to provide a string
 * representation of the size.
 * </p>
 * 
 * <p>
 * This class is annotated with {@code @AllArgsConstructor} and
 * {@code @NoArgsConstructor} to generate constructors, and
 * {@code @AerospikeRecord} to indicate that it is an Aerospike record.
 * </p>
 * 
 * @author
 */
@AllArgsConstructor
@NoArgsConstructor
@AerospikeRecord
public class Size {
    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Size{" + "width=" + width + ", height=" + height + '}';
    }
}

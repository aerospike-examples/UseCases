package com.aerospike.usecases.model;

/**
 * Enum representing the status of a line item in a real-time bidding system.
 * 
 * <ul>
 * <li>{@code ACTIVE} - The line item is currently active and participating in
 * bidding.</li>
 * <li>{@code DRAFT} - The line item is in draft mode and not yet active.</li>
 * <li>{@code FINISHED} - The line item has finished its lifecycle and is no
 * longer active.</li>
 * <li>{@code PAUSED} - The line item is temporarily paused and not
 * participating in bidding.</li>
 * </ul>
 */
public enum LineitemStatus {
    ACTIVE, DRAFT, FINISHED, PAUSED
}

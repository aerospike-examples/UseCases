package com.aerospike.usecases.model;

/**
 * Enum representing the status of a campaign in the real-time bidding system.
 * 
 * <ul>
 * <li>{@code ACTIVE} - The campaign is currently active and running.</li>
 * <li>{@code DRAFT} - The campaign is in draft mode and not yet active.</li>
 * <li>{@code FINISHED} - The campaign has finished running.</li>
 * <li>{@code PAUSED} - The campaign is temporarily paused.</li>
 * </ul>
 */
public enum CampaignStatus {
    ACTIVE, DRAFT, FINISHED, PAUSED
}

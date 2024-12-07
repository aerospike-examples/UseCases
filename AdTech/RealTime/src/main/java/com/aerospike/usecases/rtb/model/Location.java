
package com.aerospike.usecases.rtb.model;

import java.util.HashMap;
import java.util.Map;

import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.NoArgsConstructor;

/**
 * Represents a geographic location with details such as country, ISO code,
 * region, city, postal code, latitude, and longitude.
 * <p>
 * This class is used to store and retrieve information about a specific
 * location.
 * </p>
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * {@code
 * Location location = new Location("USA", "US", "California", "San Francisco", "94103", 37.7749, -122.4194);
 * System.out.println(location.getCity()); // Outputs: San Francisco
 * }
 * </pre>
 * 
 * <p>
 * The fields in this class are:
 * </p>
 * <ul>
 * <li>{@code country} - The name of the country.</li>
 * <li>{@code isoCode} - The ISO 3166-1 alpha-2 code of the country.</li>
 * <li>{@code region} - The state, province, or region.</li>
 * <li>{@code city} - The name of the city.</li>
 * <li>{@code postalCode} - The postal code or zip code.</li>
 * <li>{@code latitude} - The latitude coordinate.</li>
 * <li>{@code longitude} - The longitude coordinate.</li>
 * </ul>
 * 
 * <p>
 * This class also provides a {@code toString} method for a string
 * representation of the location.
 * </p>
 * 
 * <p>
 * Annotations used:
 * </p>
 * <ul>
 * <li>{@code @NoArgsConstructor} - Generates a no-argument constructor.</li>
 * <li>{@code @AerospikeRecord} - Indicates that this class is an Aerospike
 * record.</li>
 * </ul>
 */
@NoArgsConstructor
@AerospikeRecord
public class Location {
    private String country;
    // ISO 3166-1 alpha-2 code
    private String isoCode;
    // state, province, or region
    private String region;
    private String city;
    // postal code or zip code
    private String postalCode;
    private double latitude;
    private double longitude;

    public Location(String country, String isoCode, String region, String city, String postalCode, double latitude,
            double longitude) {
        this.country = country;
        this.isoCode = isoCode;
        this.region = region;
        this.city = city;
        this.postalCode = postalCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getRegion() {
        return region;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Location{" + "country='" + country + '\'' + ", isoCode='" + isoCode + '\'' + ", region='" + region
                + '\'' + ", city='" + city + '\'' + ", postalCode='" + postalCode + '\'' + ", latitude=" + latitude
                + ", longitude=" + longitude + '}';
    }

    public Map<String, Object> asMap() {
        // for native storage manager
        Map<String, Object> map = new HashMap<>();
        map.put("country", country);
        map.put("isoCode", isoCode);
        map.put("region", region);
        map.put("city", city);
        map.put("postalCode", postalCode);
        map.put("latitude", latitude);
        map.put("longitude", longitude);
        return map;
    }

    public static Location fromMap(Map<?, ?> map) {
        // for native storage manager
        return new Location((String) map.get("country"), (String) map.get("isoCode"), (String) map.get("region"),
                (String) map.get("city"), (String) map.get("postalCode"), (double) map.get("latitude"),
                (double) map.get("longitude"));
    }

}

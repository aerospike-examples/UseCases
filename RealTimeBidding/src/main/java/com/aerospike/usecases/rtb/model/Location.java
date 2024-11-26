
package com.aerospike.usecases.rtb.model;

import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.NoArgsConstructor;

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

}

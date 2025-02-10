package com.aerospike.usecases.model.bid;

public class Geo {
    private Float lat;
    private Float lon;
    private String country;
    private String region;
    private String regionFips104;
    private String metro;
    private String city;
    private String zip;
    private Integer utcoffset;

    // Getters and Setters
    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionFips104() {
        return regionFips104;
    }

    public void setRegionFips104(String regionFips104) {
        this.regionFips104 = regionFips104;
    }

    public String getMetro() {
        return metro;
    }

    public void setMetro(String metro) {
        this.metro = metro;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Integer getUtcoffset() {
        return utcoffset;
    }

    public void setUtcoffset(Integer utcoffset) {
        this.utcoffset = utcoffset;
    }
}

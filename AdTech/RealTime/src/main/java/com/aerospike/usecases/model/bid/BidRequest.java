
package com.aerospike.usecases.model.bid;

import java.util.List;

import com.aerospike.usecases.model.Publisher;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BidRequest {
    private String id;
    private String auctionId;
    private String siteId;
    private String appId;
    private String deviceId;
    private String userId;
    private String ipAddress;
    private String userAgent;
    private String adUnitCode;
    List<String> adFormats;
    List<String> adSizes;
    List<String> adCategories;
    List<String> adKeywords;
    String bidFloor;
    String currency;
    String country;
    String region;
    String city;
    String zip;
    double latitude;
    double longitude;
    String timestamp;
    Publisher publisher;

    public BidRequest(String id2, String userId, String auctionId, String siteId, String appId, String deviceId,
            String ipAddress, String userAgent, String adUnitCode, List<String> adFormats, List<String> adSizes,
            List<String> adCategories, List<String> adKeywords, String bidFloor, String currency, String country,
            String region, String city, String zip, double latitude, double longitude, Publisher publisher) {

        this.id = id2;
        this.userId = userId;
        this.auctionId = auctionId;
        this.siteId = siteId;
        this.appId = appId;
        this.deviceId = deviceId;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.adUnitCode = adUnitCode;
        this.adFormats = adFormats;
        this.adSizes = adSizes;
        this.adCategories = adCategories;
        this.adKeywords = adKeywords;
        this.bidFloor = bidFloor;
        this.currency = currency;
        this.country = country;
        this.region = region;
        this.city = city;
        this.zip = zip;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = String.valueOf(System.currentTimeMillis());
        this.publisher = publisher;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAdUnitCode() {
        return adUnitCode;
    }

    public void setAdUnitCode(String adUnitCode) {
        this.adUnitCode = adUnitCode;
    }

    public List<String> getAdFormats() {
        return adFormats;
    }

    public void setAdFormats(List<String> adFormats) {
        this.adFormats = adFormats;
    }

    public List<String> getAdSizes() {
        return adSizes;
    }

    public void setAdSizes(List<String> adSizes) {
        this.adSizes = adSizes;
    }

    public List<String> getAdCategories() {
        return adCategories;
    }

    public void setAdCategories(List<String> adCategories) {
        this.adCategories = adCategories;
    }

    public List<String> getAdKeywords() {
        return adKeywords;
    }

    public void setAdKeywords(List<String> adKeywords) {
        this.adKeywords = adKeywords;
    }

    public String getBidFloor() {
        return bidFloor;
    }

    public void setBidFloor(String bidFloor) {
        this.bidFloor = bidFloor;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
}

// Additional classes like Metric, Banner, Video, Audio, Native, Pmp, Publisher,
// Content, Geo, Data
// would be defined similarly with their respective fields and getters/setters.
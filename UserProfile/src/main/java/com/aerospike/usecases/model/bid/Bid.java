package com.aerospike.usecases.model.bid;

import java.util.List;

/**
 * Represents a bid in the real-time bidding system.
 * 
 * @param id      Unique identifier for the bid.
 * @param impid   Identifier for the impression to which this bid applies.
 * @param price   Bid price expressed as CPM (cost per thousand impressions).
 * @param nurl    Win notice URL called by the exchange if the bid wins.
 * @param iurl    URL of an image that is representative of the content of the
 *                campaign for ad quality/safety checking.
 * @param adomain Advertiser's primary or top-level domain for advertiser
 *                checking.
 * @param cid     Campaign ID to assist with ad quality checking.
 * @param crid    Creative ID to assist with ad quality checking.
 * @param attr    List of creative attributes.
 */
public class Bid {
    private String id;
    private String impid;
    private double price;
    private String nurl;
    private String iurl;
    private List<String> adomain;
    private String cid;
    private String crid;
    private List<Integer> attr;

    public Bid(String id, String impid, double price, String nurl, String iurl, List<String> adomain, String cid,
            String crid, List<Integer> attr) {
        this.id = id;
        this.impid = impid;
        this.price = price;
        this.nurl = nurl;
        this.iurl = iurl;
        this.adomain = adomain;
    }

    public Bid(String string, String id2, String adUnitCode, String bidFloor, String currency, String id3,
            String country, String region, String city, String postalCode, double latitude, double longitude) {
        // TODO Auto-generated constructor stub
    }

    public String getId() {
        return id;
    }

    public String getImpid() {
        return impid;
    }

    public double getPrice() {
        return price;
    }

    public String getNurl() {
        return nurl;
    }

    public String getIurl() {
        return iurl;
    }

    public List<String> getAdomain() {
        return adomain;
    }

    public String getCid() {
        return cid;
    }

    public String getCrid() {
        return crid;
    }

    public List<Integer> getAttr() {
        return attr;
    }

    @Override
    public String toString() {
        return "Bid{" + "id='" + id + '\'' + ", impid='" + impid + '\'' + ", price=" + price + ", nurl='" + nurl + '\''
                + ", iurl='" + iurl + '\'' + ", adomain=" + adomain + ", cid='" + cid + '\'' + ", crid='" + crid + '\''
                + ", attr=" + attr + '}';
    }
}
package com.aerospike.usecases.rtb.model.bid;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;

/**
 * Represents a bid response in the real-time bidding system.
 */
public class BidResponse {
    /**
     * Unique ID of the bid response.
     */
    private String id;

    /**
     * Array of seatbid objects; each contains one or more bid objects.
     */
    private List<SeatBid> seatbids;

    /**
     * ID of the impression justifying this bid.
     */
    private String impid;

    /**
     * Currrency
     */
    private String cur;

    /**
     * Bid price expressed as CPM.
     */
    private double price;

    /**
     * Win notice URL called by the exchange if the bid wins.
     */
    private String nurl;

    /**
     * Billing notice URL called by the exchange when a winning bid becomes
     * billable.
     */
    private String burl;

    /**
     * Loss notice URL called by the exchange when a bid is known to have been lost.
     */
    private String lurl;

    /**
     * Optional means of conveying ad markup in case the bid wins; supersedes the
     * win notice if markup is included in both.
     */
    private String adm;

    /**
     * ID of the ad to be served if the bid wins.
     */
    private String adid;

    /**
     * Advertiser domain for block list checking (e.g., “ford.com”).
     */
    private String adomain;

    /**
     * A platform-specific application identifier intended to be unique to the app
     * and independent of the exchange.
     */
    private String bundle;

    /**
     * URL without cache-busting to an image that is representative of the content
     * of the campaign for ad quality/safety checking.
     */
    private String iurl;

    /**
     * Campaign ID to assist with ad quality checking.
     */
    private String cid;

    /**
     * Creative ID to assist with ad quality checking.
     */
    private String crid;

    /**
     * Tactic ID to enable buyers to label bids for reporting to the exchange.
     */
    private String tactic;

    /**
     * IAB content categories of the creative.
     */
    private String cat;

    /**
     * Array of creative attributes.
     */
    private String attr;

    /**
     * API required by the markup if applicable.
     */
    private String api;

    /**
     * Video response protocol of the markup if applicable.
     */
    private String protocol;

    /**
     * Media rating per IQG guidelines.
     */
    private String qagmediarating;

    /**
     * Language of the creative using ISO-639-1-alpha-2.
     */
    private String language;

    /**
     * Deal ID extension of the bid response.
     */
    private String dealid;

    /**
     * Width of the creative in device independent pixels (DIPS).
     */
    private int w;

    /**
     * Height of the creative in device independent pixels (DIPS).
     */
    private int h;

    /**
     * Relative width of the creative when expressing size as a ratio.
     */
    private int wratio;

    /**
     * Relative height of the creative when expressing size as a ratio.
     */
    private int hratio;

    /**
     * Advisory as to the number of seconds the bidder is willing to wait between
     * the auction and the actual impression.
     */
    private int exp;

    /**
     * Bid ID to assist with ad quality checking; the response should echo back the
     */
    private String bidid;

    public BidResponse(String bidid, String cur, List<SeatBid> seatbids) {
        this();
        this.bidid = bidid;
        this.cur = cur;
        this.seatbids = seatbids;
    }

    public BidResponse() {
        this.id = UUID.randomUUID().toString();
        this.nurl = "http://localhost:8080/win"; // Mock win notice URL
        this.impid = UUID.randomUUID().toString(); // Mock impression ID
        this.price = 0.53; // Mock price
        this.adm = "<html><body><h1>Buy now!</h1></body></html>"; // Mock ad markup
        this.w = 300; // Mock width
        this.h = 250; // Mock height
        this.exp = 300; // Mock advisory

    }

    public String getId() {
        return id;
    }

    public List<SeatBid> getSeatbids() {
        return seatbids;
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

    public String getBurl() {
        return burl;
    }

    public String getLurl() {
        return lurl;
    }

    public String getAdm() {
        return adm;
    }

    public String getAdid() {
        return adid;
    }

    public String getAdomain() {
        return adomain;
    }

    public String getBundle() {
        return bundle;
    }

    public String getIurl() {
        return iurl;
    }

    public String getCid() {
        return cid;
    }

    public String getCrid() {
        return crid;
    }

    public String getTactic() {
        return tactic;
    }

    public String getCat() {
        return cat;
    }

    public String getAttr() {
        return attr;
    }

    public String getApi() {
        return api;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getQagmediarating() {
        return qagmediarating;
    }

    public String getLanguage() {
        return language;
    }

    public String getDealid() {
        return dealid;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public int getWratio() {
        return wratio;
    }

    public int getHratio() {
        return hratio;
    }

    public int getExp() {
        return exp;
    }

    @Override
    public String toString() {
        return "BidResponse{" + "id='" + id + '\'' + ", seatbids='" + seatbids + '\'' + '\'' + ", impid='" + impid
                + '\'' + ", price=" + price + ", nurl='" + nurl + '\'' + ", burl='" + burl + '\'' + ", lurl='" + lurl
                + '\'' + ", adm='" + adm + '\'' + ", adid='" + adid + '\'' + ", adomain='" + adomain + '\''
                + ", bundle='" + bundle + '\'' + ", iurl='" + iurl + '\'' + ", cid='" + cid + '\'' + ", crid='" + crid
                + '\'' + ", tactic='" + tactic + '\'' + ", cat='" + cat + '\'' + ", attr='" + attr + '\'' + ", api='"
                + api + '\'' + ", protocol='" + protocol + '\'' + ", qagmediarating='" + qagmediarating + '\''
                + ", language='" + language + '\'' + ", dealid='" + dealid + '\'' + ", w=" + w + ", h=" + h
                + ", wratio=" + wratio + ", hratio=" + hratio + ", exp=" + exp + '}';
    }
}

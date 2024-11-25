package com.aerospike.usecases.rtb.model.bid;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class Imp {
    private String id;
    // private Metric metric;
    // private Banner banner;
    // private Video video;
    // private Audio audio;
    // private Native aNative;
    // private Pmp pmp;
    private String displaymanager;
    private String displaymanagerver;
    private int instl;
    private String tagid;
    private double bidfloor;
    private String bidfloorcur;
    private int clickbrowser;
    private int secure;
    // private List<String> iframebuster;
    private Ext ext;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplaymanager() {
        return displaymanager;
    }

    public void setDisplaymanager(String displaymanager) {
        this.displaymanager = displaymanager;
    }

    public String getDisplaymanagerver() {
        return displaymanagerver;
    }

    public void setDisplaymanagerver(String displaymanagerver) {
        this.displaymanagerver = displaymanagerver;
    }

    public int getInstl() {
        return instl;
    }

    public void setInstl(int instl) {
        this.instl = instl;
    }

    public String getTagid() {
        return tagid;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public double getBidfloor() {
        return bidfloor;
    }

    public void setBidfloor(double bidfloor) {
        this.bidfloor = bidfloor;
    }

    public String getBidfloorcur() {
        return bidfloorcur;
    }

    public void setBidfloorcur(String bidfloorcur) {
        this.bidfloorcur = bidfloorcur;
    }

    public int getClickbrowser() {
        return clickbrowser;
    }

    public void setClickbrowser(int clickbrowser) {
        this.clickbrowser = clickbrowser;
    }

    public int getSecure() {
        return secure;
    }

    public void setSecure(int secure) {
        this.secure = secure;
    }

    public Ext getExt() {
        return ext;
    }

    public void setExt(Ext ext) {
        this.ext = ext;
    }
}

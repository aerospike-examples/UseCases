package com.aerospike.usecases.rtb.model.bid;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class Device {
    private String ua;
    private Geo geo;
    private String dnt;
    private String lmt;
    private String ip;
    private String ipv6;
    private String devicetype;
    private String make;
    private String model;
    private String os;
    private String osv;
    private String hwv;
    private String h;
    private String w;
    private String ppi;
    private String pxratio;
    private String js;
    private String geofetch;
    private String flashver;
    private String language;
    private String carrier;
    private String mccmnc;
    private String connectiontype;
    private String ifa;
    private String didsha1;
    private String didmd5;
    private String dpidsha1;
    private String dpidmd5;
    private String macsha1;
    private String macmd5;
    private Ext ext;

    // Getters and Setters
    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public String getDnt() {
        return dnt;
    }

    public void setDnt(String dnt) {
        this.dnt = dnt;
    }

    public String getLmt() {
        return lmt;
    }

    public void setLmt(String lmt) {
        this.lmt = lmt;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIpv6() {
        return ipv6;
    }

    public void setIpv6(String ipv6) {
        this.ipv6 = ipv6;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsv() {
        return osv;
    }

    public void setOsv(String osv) {
        this.osv = osv;
    }

    public String getHwv() {
        return hwv;
    }

    public void setHwv(String hwv) {
        this.hwv = hwv;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getPpi() {
        return ppi;
    }

    public void setPpi(String ppi) {
        this.ppi = ppi;
    }

    public String getPxratio() {
        return pxratio;
    }

    public void setPxratio(String pxratio) {
        this.pxratio = pxratio;
    }

    public String getJs() {
        return js;
    }

    public void setJs(String js) {
        this.js = js;
    }

    public String getGeofetch() {
        return geofetch;
    }

    public void setGeofetch(String geofetch) {
        this.geofetch = geofetch;
    }

    public String getFlashver() {
        return flashver;
    }

    public void setFlashver(String flashver) {
        this.flashver = flashver;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getMccmnc() {
        return mccmnc;
    }

    public void setMccmnc(String mccmnc) {
        this.mccmnc = mccmnc;
    }

    public String getConnectiontype() {
        return connectiontype;
    }

    public void setConnectiontype(String connectiontype) {
        this.connectiontype = connectiontype;
    }

    public String getIfa() {
        return ifa;
    }

    public void setIfa(String ifa) {
        this.ifa = ifa;
    }

    public String getDidsha1() {
        return didsha1;
    }

    public void setDidsha1(String didsha1) {
        this.didsha1 = didsha1;
    }

    public String getDidmd5() {
        return didmd5;
    }

    public void setDidmd5(String didmd5) {
        this.didmd5 = didmd5;
    }

    public String getDpidsha1() {
        return dpidsha1;
    }

    public void setDpidsha1(String dpidsha1) {
        this.dpidsha1 = dpidsha1;
    }

    public String getDpidmd5() {
        return dpidmd5;
    }

    public void setDpidmd5(String dpidmd5) {
        this.dpidmd5 = dpidmd5;
    }

    public String getMacsha1() {
        return macsha1;
    }

    public void setMacsha1(String macsha1) {
        this.macsha1 = macsha1;
    }

    public String getMacmd5() {
        return macmd5;
    }

    public void setMacmd5(String macmd5) {
        this.macmd5 = macmd5;
    }

    public Ext getExt() {
        return ext;
    }

    public void setExt(Ext ext) {
        this.ext = ext;
    }
}

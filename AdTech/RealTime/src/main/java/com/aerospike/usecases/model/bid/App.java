package com.aerospike.usecases.model.bid;

import java.util.List;

import com.aerospike.usecases.model.Publisher;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class App {
    private String id;
    private String name;
    private String bundle;
    private String domain;
    private String storeurl;
    private List<String> cat;
    private List<String> sectioncat;
    private List<String> pagecat;
    private String ver;
    private int privacypolicy;
    private int paid;
    private Publisher publisher;
    // private Content content;
    private String keywords;
    private Ext ext;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getStoreurl() {
        return storeurl;
    }

    public void setStoreurl(String storeurl) {
        this.storeurl = storeurl;
    }

    public List<String> getCat() {
        return cat;
    }

    public void setCat(List<String> cat) {
        this.cat = cat;
    }

    public List<String> getSectioncat() {
        return sectioncat;
    }

    public void setSectioncat(List<String> sectioncat) {
        this.sectioncat = sectioncat;
    }

    public List<String> getPagecat() {
        return pagecat;
    }

    public void setPagecat(List<String> pagecat) {
        this.pagecat = pagecat;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public int getPrivacypolicy() {
        return privacypolicy;
    }

    public void setPrivacypolicy(int privacypolicy) {
        this.privacypolicy = privacypolicy;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Ext getExt() {
        return ext;
    }

    public void setExt(Ext ext) {
        this.ext = ext;
    }
}

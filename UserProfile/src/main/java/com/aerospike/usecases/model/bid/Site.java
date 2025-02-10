package com.aerospike.usecases.model.bid;

import java.util.List;

import com.aerospike.usecases.model.Publisher;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class Site {
    private String id;
    private String name;
    private String domain;
    private List<String> cat;
    private List<String> sectioncat;
    private List<String> pagecat;
    private String page;
    private String ref;
    private String search;
    private int mobile;
    private int privacypolicy;
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public int getPrivacypolicy() {
        return privacypolicy;
    }

    public void setPrivacypolicy(int privacypolicy) {
        this.privacypolicy = privacypolicy;
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

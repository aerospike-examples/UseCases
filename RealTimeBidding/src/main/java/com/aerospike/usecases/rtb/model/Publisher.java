package com.aerospike.usecases.rtb.model;

import java.util.List;

import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@AerospikeRecord
public class Publisher {
    private String id;
    private String name;
    private String domain;
    private List<String> categories;
    private List<String> domains;
    private List<String> keywords;

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

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getDomains() {
        return domains;
    }

    public void setDomains(List<String> domains) {
        this.domains = domains;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}

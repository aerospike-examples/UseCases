package com.aerospike.usecases.model;

import java.util.List;

import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Publisher in the Real-Time Bidding system. A Publisher can be a
 * web or app publisher.
 * 
 * <p>
 * This class is annotated with {@code @AllArgsConstructor} and
 * {@code @NoArgsConstructor} to generate constructors with and without
 * parameters. It is also annotated with {@code @AerospikeRecord} to indicate
 * that it is an Aerospike record.
 * </p>
 * 
 * <p>
 * Attributes:
 * </p>
 * <ul>
 * <li>{@code id} - Unique identifier for the publisher.</li>
 * <li>{@code name} - Name of the publisher.</li>
 * <li>{@code domain} - Domain of the publisher.</li>
 * <li>{@code categories} - List of categories associated with the
 * publisher.</li>
 * <li>{@code domains} - List of domains associated with the publisher.</li>
 * <li>{@code keywords} - List of keywords associated with the publisher.</li>
 * </ul>
 * 
 * <p>
 * Includes getter and setter methods for all attributes, and overrides the
 * {@code toString} method to provide a string representation of the Publisher
 * object.
 * </p>
 */
@NoArgsConstructor
@AerospikeRecord
public class Publisher {
    private String id;
    private String name;
    private String domain;
    private List<String> categories;
    private List<String> domains;
    private List<String> keywords;

    public Publisher(String id, String name, String domain, List<String> categories, List<String> domains,
            List<String> keywords) {
        this.id = id;
        this.name = name;
        this.domain = domain;
        this.categories = categories;
        this.domains = domains;
        this.keywords = keywords;
    }

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

    @Override
    public String toString() {
        return "Publisher{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", domain='" + domain + '\''
                + ", categories=" + categories + ", domains=" + domains + ", keywords=" + keywords + '}';
    }

    public Map<String, Object> asMap() {
        // for native storage manager
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("domain", domain);
        map.put("categories", categories);
        map.put("domains", domains);
        map.put("keywords", keywords);
        return map;
    }

    @SuppressWarnings("unchecked")
    public static Publisher fromMap(Map<String, Object> map) {
        // for native storage manager
        Publisher publisher = new Publisher((String) map.get("id"), (String) map.get("name"),
                (String) map.get("domain"), (List<String>) map.get("categories"), (List<String>) map.get("domains"),
                (List<String>) map.get("keywords"));
        return publisher;
    }
}

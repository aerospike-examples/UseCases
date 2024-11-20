package com.aerospike.usecases.rtb.model;

import java.util.ArrayList;
import java.util.List;

public class Audience {
    private String id;
    private List<Segment> segments;

    public Audience(String id) {
        this.segments = new ArrayList<Segment>();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void addSegment(Segment segment) {
        this.segments.add(segment);
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }
}

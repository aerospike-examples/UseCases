package com.aerospike.usecases.model.bid;

import java.util.List;

public class SeatBid {
    private String seat;
    private List<Bid> bid;

    public SeatBid(String seat, List<Bid> bid) {
        this.seat = seat;
        this.bid = bid;
    }

    public String getSeat() {
        return seat;
    }

    public List<Bid> getBid() {
        return bid;
    }

    @Override
    public String toString() {
        return "SeatBid{" + "seat='" + seat + '\'' + ", bid=" + bid + '}';
    }
}

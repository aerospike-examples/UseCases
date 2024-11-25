package com.aerospike.usecases.rtb.model.bid;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class Regs {
    private int coppa;
    private Ext ext;

    // Getters and Setters
    public int getCoppa() {
        return coppa;
    }

    public void setCoppa(int coppa) {
        this.coppa = coppa;
    }

    public Ext getExt() {
        return ext;
    }

    public void setExt(Ext ext) {
        this.ext = ext;
    }
}

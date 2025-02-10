package com.aerospike.usecases.model.bid;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class Source {
    private String fd;
    private String tid;
    private String pchain;
    private Ext ext;

    // Getters and Setters
    public String getFd() {
        return fd;
    }

    public void setFd(String fd) {
        this.fd = fd;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getPchain() {
        return pchain;
    }

    public void setPchain(String pchain) {
        this.pchain = pchain;
    }

    public Ext getExt() {
        return ext;
    }

    public void setExt(Ext ext) {
        this.ext = ext;
    }
}

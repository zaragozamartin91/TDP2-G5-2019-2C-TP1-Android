package com.g5.tdp2.myhealthapp.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Check {
    private int id;
    private String url;
    private String path;
    private String status;
    private int specialtyId;
    private int affiliateId;

    @JsonCreator
    public Check(
            @JsonProperty("id") int id,
            @JsonProperty("url") String url,
            @JsonProperty("path") String path,
            @JsonProperty("status") String status,
            @JsonProperty("specialty_id") int specialtyId,
            @JsonProperty("affiliate_id") int affiliateId) {
        this.id = id;
        this.url = url;
        this.path = path;
        this.status = status;
        this.specialtyId = specialtyId;
        this.affiliateId = affiliateId;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getPath() {
        return path;
    }

    public String getStatus() {
        return status;
    }

    public int getSpecialtyId() {
        return specialtyId;
    }

    public int getAffiliateId() {
        return affiliateId;
    }
}

package com.g5.tdp2.myhealthapp.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

public class Check implements Serializable {
    private long id;
    private String url;
    private String path;
    private String status;
    private long specialtyId;
    private long affiliateId;
    private final Date updatedAt;
    private final Date createdAt;

    @JsonCreator
    public Check(
            @JsonProperty("id") long id,
            @JsonProperty("url") String url,
            @JsonProperty("path") String path,
            @JsonProperty("status") String status,
            @JsonProperty("specialty_id") long specialtyId,
            @JsonProperty("affiliate_id") long affiliateId,
            @JsonProperty("created_at") @JsonDeserialize(using = YyyymmddDeserializer.class) Date createdAt,
            @JsonProperty("updated_at") @JsonDeserialize(using = YyyymmddDeserializer.class) Date updatedAt) {
        this.id = id;
        this.url = url;
        this.path = path;
        this.status = status;
        this.specialtyId = specialtyId;
        this.affiliateId = affiliateId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {
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

    public long getSpecialtyId() {
        return specialtyId;
    }

    public long getAffiliateId() {
        return affiliateId;
    }

    public Date getUpdatedAt() { return updatedAt; }

    public Date getCreatedAt() { return createdAt; }

    @JsonIgnore
    public String translateStatus() {
        String s = Optional.ofNullable(status).orElse("");
        switch (s.toUpperCase()) {
            case "PENDING":
                return "PENDIENTE";
            case "CANCELLED":
            case "CANCELED":
                return "CANCELADA";
            case "ACCEPTED":
            case "APPROVED":
                return "APROBADA";
            case "MISSING_INFO":
            case "MISSING_DATA":
            case "MISSING INFO":
            case "MISSING DATA":
            case "MISSINGINFO":
            case "MISSINGDATA":
                return "FALTA INFORMACION";
            default:
                return "DESCONOCIDO";
        }
    }

    @JsonIgnore
    public String translateSpecialty(Function<Long, Specialty> translator) {
        return translator.apply(getSpecialtyId()).getName();
    }

    @JsonIgnore
    public String translateCreatedAt(Function<Date, String> translator) {
        return translator.apply(getCreatedAt());
    }

    @JsonIgnore
    public String translateUpdatedAt(Function<Date, String> translator) {
        return translator.apply(getUpdatedAt());
    }
}

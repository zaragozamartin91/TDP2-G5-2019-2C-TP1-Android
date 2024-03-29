package com.g5.tdp2.myhealthapp.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

public class Check implements Serializable {
    private long id;
    private String url;
    private String path;
    private String status;
    private final long checktypeId;
    private long specialtyId;
    private long affiliateId;
    private final Date updatedAt;
    private final Date createdAt;
    private final String observations;

    @JsonCreator
    public Check(
            @JsonProperty("id") long id,
            @JsonProperty("url") String url,
            @JsonProperty("path") String path,
            @JsonProperty("status") String status,
            @JsonProperty("authtype_id") long checktypeId,
            @JsonProperty("specialty_id") long specialtyId,
            @JsonProperty("affiliate_id") long affiliateId,
            @JsonProperty("created_at") @JsonDeserialize(using = YyyymmddDeserializer.class) Date createdAt,
            @JsonProperty("updated_at") @JsonDeserialize(using = YyyymmddDeserializer.class) Date updatedAt,
            @JsonProperty("observations") String observations) {
        this.id = id;
        this.url = url;
        this.path = path;
        this.status = status;
        this.specialtyId = specialtyId;
        this.affiliateId = affiliateId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.checktypeId = checktypeId;
        this.observations = observations;
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

    public long getChecktypeId() { return checktypeId; }

    public long getSpecialtyId() {
        return specialtyId;
    }

    public long getAffiliateId() {
        return affiliateId;
    }

    public Date getUpdatedAt() { return updatedAt; }

    public Date getCreatedAt() { return createdAt; }

    public String getObservations() { return observations; }

    public String translateObs() { return Optional.ofNullable(getObservations()).orElse(""); }

    @JsonIgnore
    public String translateStatus() {
        String s = Optional.ofNullable(status).orElse("");
        switch (s.toUpperCase()) {
            case "PENDIENTE":
            case "PENDING":
                return "PENDIENTE";

            case "RECHAZADO":
            case "RECHAZADA":
            case "CANCELADA":
            case "CANCELADO":
            case "CANCELLED":
            case "CANCELED":
                return "RECHAZADO";

            case "APROBADA":
            case "APROBADO":
            case "AUTORIZADO":
            case "ACCEPTED":
            case "APPROVED":
                return "AUTORIZADO";

            case "NECESITA MAS INFORMACION":
            case "FALTA INFORMACION":
            case "MISSING_INFO":
            case "MISSING_DATA":
            case "MISSING INFO":
            case "MISSING DATA":
            case "MISSINGINFO":
            case "MISSINGDATA":
                return "NECESITA MAS INFORMACION";

            case "AUTORIZADO AUTOMATICAMENTE":
            case "APROBADO AUTOMATICAMENTE":
                return "AUTORIZADO AUTOMATICAMENTE";

            default:
                return "DESCONOCIDO";
        }
    }

    @JsonIgnore
    public String translateSpecialty(Function<Long, Specialty> translator) {
        return translator.apply(getSpecialtyId()).getName();
    }

    @JsonIgnore
    public String translateChecktype(Function<Long, Checktype> trans) {
        return Optional.of(getChecktypeId())
                .filter(ctid -> ctid > 0)
                .map(trans)
                .map(SimpleEntity::getName)
                .orElse("");
    }

    @JsonIgnore
    public String translateCreatedAt(Function<Date, String> translator) {
        return translator.apply(getCreatedAt());
    }

    @JsonIgnore
    public String translateUpdatedAt(Function<Date, String> translator) {
        return translator.apply(getUpdatedAt());
    }

    /**
     * Obtiene un comparador de estudios para ordenarlos.
     * Los estudios mas recientes van primero.
     *
     * @return comparador de estudios
     */
    @JsonIgnore
    public static Comparator<Check> comparator() {
        return (c1, c2) -> c2.createdAt.compareTo(c1.createdAt);
    }
}

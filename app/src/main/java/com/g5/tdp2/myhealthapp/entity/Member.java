package com.g5.tdp2.myhealthapp.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Member implements Serializable {
    private long id; // id del afiliado en la BBDD
    private String firstname;
    private String lastname;
    private Date birthdate;
    private String memberId; // numero de afiliado
    private String plan;
    private long idn; // DNI del afiliado
    private String email;
    private String token = "";

    @JsonCreator
    public Member(
            @JsonProperty("id") long id,
            @JsonProperty("firstname") String firstname,
            @JsonProperty("lastname") String lastname,
            @JsonProperty("birthdate") @JsonDeserialize(using = YyyymmddDeserializer.class) Date birthdate,
            @JsonProperty("affiliate_id") String memberId,
            @JsonProperty("plan") String plan,
            @JsonProperty("idn") long idn,
            @JsonProperty("email") String email) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.memberId = memberId;
        this.plan = plan;
        this.idn = idn;
        this.email = email;
    }

    /**
     * Obtiene el id del afiliado. Necesario para buscar estudios
     */
    public long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    @JsonSerialize(using = YyyymmddSerializer.class)
    public Date getBirthdate() {
        return birthdate;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getPlan() {
        return plan;
    }

    public long getIdn() {
        return idn;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    public Member withToken(String t) {
        token = t;
        return this;
    }

    @JsonIgnore
    public String getToken() {
        return token;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        if (id != member.id) return false;
        if (idn != member.idn) return false;
        if (firstname != null ? !firstname.equals(member.firstname) : member.firstname != null)
            return false;
        if (lastname != null ? !lastname.equals(member.lastname) : member.lastname != null)
            return false;
        if (birthdate != null ? !birthdate.equals(member.birthdate) : member.birthdate != null)
            return false;
        if (memberId != null ? !memberId.equals(member.memberId) : member.memberId != null)
            return false;
        if (plan != null ? !plan.equals(member.plan) : member.plan != null) return false;
        if (email != null ? !email.equals(member.email) : member.email != null) return false;
        return token != null ? token.equals(member.token) : member.token == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (birthdate != null ? birthdate.hashCode() : 0);
        result = 31 * result + (memberId != null ? memberId.hashCode() : 0);
        result = 31 * result + (plan != null ? plan.hashCode() : 0);
        result = 31 * result + (int) (idn ^ (idn >>> 32));
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", birthdate=" + birthdate +
                ", memberId='" + memberId + '\'' +
                ", plan='" + plan + '\'' +
                ", idn=" + idn +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}


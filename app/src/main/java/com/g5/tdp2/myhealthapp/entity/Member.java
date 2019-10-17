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
    private String firstname;
    private String lastname;
    private Date birthdate;
    private String memberId;
    private String plan;
    private long id;
    private String email;
    private String token = "";

    @JsonCreator
    public Member(
            @JsonProperty("firstname") String firstname,
            @JsonProperty("lastname") String lastname,
            @JsonProperty("birthdate") @JsonDeserialize(using = YyyymmddDeserializer.class) Date birthdate,
            @JsonProperty("affiliate_id") String memberId,
            @JsonProperty("plan") String plan,
            @JsonProperty("idn") long id,
            @JsonProperty("email") String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.memberId = memberId;
        this.plan = plan;
        this.id = id;
        this.email = email;
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

    public long getId() {
        return id;
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
        return id == member.id &&
                Objects.equals(firstname, member.firstname) &&
                Objects.equals(lastname, member.lastname) &&
                Objects.equals(birthdate, member.birthdate) &&
                Objects.equals(memberId, member.memberId) &&
                Objects.equals(plan, member.plan) &&
                Objects.equals(email, member.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, birthdate, memberId, plan, id, email);
    }

    @Override
    public String toString() {
        return "Member{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", birthdate=" + birthdate +
                ", memberId='" + memberId + '\'' +
                ", plan='" + plan + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}


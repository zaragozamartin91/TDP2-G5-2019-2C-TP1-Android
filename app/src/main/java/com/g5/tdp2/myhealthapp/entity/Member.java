package com.g5.tdp2.myhealthapp.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.g5.tdp2.myhealthapp.util.DateFormatter;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class Member {
    private String firstname;
    private String lastname;
    private Date birthdate;
    private String memberId;
    private String plan;
    private long id;
    private String email;

    @JsonCreator
    public Member(
            @JsonProperty("firstname") String firstname,
            @JsonProperty("lastname") String lastname,
            @JsonProperty("birthdate") @JsonDeserialize(using = MemberBirthdateDeserializer.class) Date birthdate,
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

class MemberBirthdateDeserializer extends StdDeserializer<Date> {
    public MemberBirthdateDeserializer() {
        this(null);
    }

    protected MemberBirthdateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String sbirth = node.asText();
        return DateFormatter.YYYY_MM_DD.deserialize(sbirth);
    }
}
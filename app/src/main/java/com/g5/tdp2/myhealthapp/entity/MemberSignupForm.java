package com.g5.tdp2.myhealthapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MemberSignupForm {
    public static final String INVALID_PASSWORD = "INVALID_PASSWORD";
    public static final String EMPTY_FIRST_NAME = "EMPTY_FIRST_NAME";
    public static final String EMPTY_LAST_NAME = "EMPTY_LAST_NAME";
    public static final String INVALID_EMAIL = "INVALID_EMAIL";
    public static final String INVALID_ID = "INVALID_ID";
    public static final String INVALID_MEMBER_ID = "INVALID_MEMBER_ID";
    public static final String PASSWORDS_DONT_MATCH = "PASSWORDS_DONT_MATCH";
    public static final String INVALID_PLAN = "INVALID_PLAN";

    private static final String dateFormat = "yyyy-MM-dd";

    private String firstName;
    private String lastName;
    private String email;
    private long id;
    private String memberId;
    private String plan;
    private Date birth;
    private String password;
    private String repPassword;

    public MemberSignupForm(String firstName, String lastName, String email, long id, String memberId, String plan, Date birth, String password, String repPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.id = id;
        this.memberId = memberId;
        this.plan = plan;
        this.birth = birth;
        this.password = password;
        this.repPassword = repPassword;
    }

    /**
     * Valida el formulario
     *
     * @throws IllegalStateException Si alguno de los campos no cumple con los constraints
     */
    public void validate() throws IllegalStateException {
        Validate.validState(StringUtils.isNotBlank(firstName), EMPTY_FIRST_NAME);
        Validate.validState(StringUtils.isNotBlank(lastName), EMPTY_LAST_NAME);

        Validate.validState(StringUtils.isNotBlank(email), INVALID_EMAIL);
        Validate.validState(email.matches("^\\w(\\w|\\.)+\\w@\\w(\\w|\\.)+\\w$"), INVALID_EMAIL);

        Validate.validState(id > 0L, INVALID_ID);
        Validate.validState(StringUtils.isNotBlank(memberId), INVALID_MEMBER_ID);
        Validate.validState(memberId.matches("^\\w+$"), INVALID_MEMBER_ID);

        Validate.validState(StringUtils.isNotBlank(plan), INVALID_PLAN);
        Validate.validState(plan.matches("^\\w+$"), INVALID_PLAN);


        Validate.validState(password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"), INVALID_PASSWORD);
        Validate.validState(password.equals(repPassword), PASSWORDS_DONT_MATCH);
    }

    @JsonProperty("firstname")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("lastname")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("idn")
    public long getId() {
        return id;
    }

    @JsonProperty("affiliate_id")
    public String getMemberId() {
        return memberId;
    }

    public String getPlan() {
        return plan;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("birthdate")
    public String getBirthdateAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        return sdf.format(getBirth());
    }

    @JsonIgnore
    public String getRepPassword() {
        return repPassword;
    }

    @JsonIgnore
    public Date getBirth() {
        return birth;
    }
}



/* Formulario de registro
{
	"info": {
		"firstname": "Christian",
		"lastname": "Angelone",
		"birthdate": "1990-08-31",
		"affiliate_id": "asd123asd456",
		"idn": 35317588,
		"email": "christiangelone@gmail.com",
		"password": "qwerty"
	},
	"role": "affiliate"
} */
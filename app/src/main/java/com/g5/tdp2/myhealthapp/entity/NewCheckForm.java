package com.g5.tdp2.myhealthapp.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class NewCheckForm {
    private static final String INVALID_URL = "INVALID_URL";
    private static final String INVALID_SPECIALTY = "INVALID_SPECIALTY";
    private static final String INVALID_PATH = "INVALID_PATH";
    private static final String INVALID_CHECKTYPE = "INVALID_CHECKTYPE";

    private String url;
    private String path;
    private long specId;
    private long userId;
    private Boolean preAuthorize = null;
    private final long checktype;

    @JsonCreator
    public NewCheckForm(
            @JsonProperty("url") String url,
            @JsonProperty("path") String path,
            @JsonProperty("specialty_id") long specId,
            @JsonProperty("affiliate_id") long userId,
            @JsonProperty("authtype_id") long checktype) {
        this.url = url;
        this.path = path;
        this.specId = specId;
        this.userId = userId;
        this.checktype = checktype;
    }

    public String getUrl() {
        return url;
    }

    public String getPath() {
        return path;
    }

    @JsonProperty("specialty_id")
    public long getSpecId() {
        return specId;
    }

    @JsonProperty("affiliate_id")
    public long getUserId() {
        return userId;
    }

    @JsonProperty("authtype_id")
    public long getChecktype() { return checktype; }

    @JsonProperty("authorize")
    public Boolean getPreAuthorize() {
        return preAuthorize;
    }

    @JsonIgnore
    public NewCheckForm setPreAuthorize(Boolean preAuthorize) {
        this.preAuthorize = preAuthorize;
        return this;
    }

    @JsonIgnore
    public void validate() {
        Validate.validState(StringUtils.isNotBlank(path), INVALID_PATH);
        Validate.validState(StringUtils.isNotBlank(url), INVALID_URL);
        Validate.validState(specId > 0, INVALID_SPECIALTY);
        Validate.validState(checktype > 0, INVALID_CHECKTYPE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewCheckForm that = (NewCheckForm) o;

        if (specId != that.specId) return false;
        if (userId != that.userId) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;
        return preAuthorize != null ? preAuthorize.equals(that.preAuthorize) : that.preAuthorize == null;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (int) (specId ^ (specId >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (preAuthorize != null ? preAuthorize.hashCode() : 0);
        return result;
    }
}


/* {
  "url":"https://firebasestorage.googleapis.com/v0/b/lustrous-bay-252022.appspot.com/o/myhealthapp%2Fchecks%2Fpending%2F962f87c9-3ea7-4770-bd89-b0e0817c32fe.jpg?alt=media&token=a451095b-212a-4be3-80ef-04ac9c178b1f",
  "path":"myhealthapp/checks/pending/962f87c9-3ea7-4770-bd89-b0e0817c32fe.jpg",
  "specialty_id":14,
  "affiliate_id": 123
} */

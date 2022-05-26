package com.santander.cross.fra.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.IgnoreProperty;
import org.jboss.resteasy.reactive.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FRAJwt {

    //@FormParam("JWTToken")
    @JsonProperty("JWTToken")
    private String jwttoken;

    public String getJwttoken() {
        return jwttoken;
    }

    public void setJwttoken(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    @Override
    public String toString() {
        return "FRAJwt{" +
                "jwttoken='" + jwttoken + '\'' +
                '}';
    }
}

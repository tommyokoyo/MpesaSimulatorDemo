package com.openhub.mpesasimulatordemo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthenticationResponse {
    @JsonProperty("access_token")
    private String access_token;
}

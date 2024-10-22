package com.openhub.mpesasimulatordemo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CallbackResponse {
    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
}

package com.openhub.mpesasimulatordemo.models;

import lombok.Getter;

@Getter
public enum ResponseMessage {
    SUCCESS("The service request has been accepted successfully."),
    FAILED("The service request has failed"),
    INVALID("The service request has invalid parameters."),
    UNAUTHORIZED("Invalid Access Token");

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }
}

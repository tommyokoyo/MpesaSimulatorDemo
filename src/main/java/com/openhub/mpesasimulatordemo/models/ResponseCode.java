package com.openhub.mpesasimulatordemo.models;

import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS(0),
    FAILED(1),
    SERVER_ERROR(500),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    CANCELED_BY_USER(1032);

    private final int responseCode;

    ResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}

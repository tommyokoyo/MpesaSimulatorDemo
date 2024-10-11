package com.openhub.mpesasimulatordemo.models;

import lombok.Data;

@Data
public class ErrorResponseBuild {
    private String ResponseCode;
    private String ResponseDescription;
    private String CustomerMessage;
}

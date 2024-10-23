package com.openhub.mpesasimulatordemo.models;

import lombok.Data;

@Data
public class MsimStkResponse {
    private String MerchantRequestID;
    private String CheckoutRequestID;
    private String ResponseCode;
    private String ResponseDescription;
    private String CustomerMessage;
}

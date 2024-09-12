package com.openhub.mpesasimulatordemo.models;

import lombok.Data;

@Data
public class MpesaCallbackMessage {
    private String MerchantRequestID;
    private String CheckoutRequestID;
    private int ResultCode;
    private String ResultDesc;
    private Double transactionAmount;
    private String transactionID;
}

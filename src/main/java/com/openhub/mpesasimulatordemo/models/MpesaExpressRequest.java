package com.openhub.mpesasimulatordemo.models;

import lombok.Data;

@Data
public class MpesaExpressRequest {
    private String BusinessShortCode;
    private String Password;
    private String Timestamp;
    private TransactionType TransactionType;
    private String Amount;
    private String PartyA;
    private String PartyB;
    private String PhoneNumber;
    private String CallBackURL;
    private String AccountReference;
    private String TransactionDesc;
}

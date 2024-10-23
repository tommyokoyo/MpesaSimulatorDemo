package com.openhub.mpesasimulatordemo.models;

import lombok.Data;

@Data
public class TransactionMessage {
    private String transactionType;
    private Double amount;
    private String partyA;
    private String partyB;
    private String phoneNumber;
    private String callBackUrl;
    private String accountReference;
    private String transactionDescription;
    private String merchantRequestID;
    private String checkOutRequestID;
}

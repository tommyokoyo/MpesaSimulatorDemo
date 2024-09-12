package com.openhub.mpesasimulatordemo.models;

import lombok.Data;

@Data
public class TransactionMessage {
    private String transactionId;
    private Double amount;
    private String phoneNumber;
    private String status;
}

package com.openhub.mpesasimulatordemo.models;

import lombok.Data;

@Data
public class TransactionStatusQueryResponse {
    private String originatorConversationID;
    private String conversationID;
    private String responseCode;
    private String responseDescription;
}

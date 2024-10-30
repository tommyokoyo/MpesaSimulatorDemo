package com.openhub.mpesasimulatordemo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransactionStatusQueryRequest {
    @JsonProperty("Initiator")
    private String initiator;
    @JsonProperty("SecurityCredential")
    private String securityCredential;
    @JsonProperty("Command ID")
    private String commandID;
    @JsonProperty("Transaction ID")
    private String transactionID;
    @JsonProperty("OriginatorConversationID")
    private String originatorConversationID;
    @JsonProperty("PartyA")
    private String partyA;
    @JsonProperty("IdentifierType")
    private String identifierType;
    @JsonProperty("ResultURL")
    private String resultURL;
    @JsonProperty("QueueTimeOutURL")
    private String queueTimeOutURL;
    @JsonProperty("Remarks")
    private String remarks;
    @JsonProperty("Occasion")
    private String occasions;
}

package com.openhub.mpesasimulatordemo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(
        name = "callback_message"
)
public class CallBackMessage {
    @Id
    @Column(
            name = "merchantrequestid",
            nullable = false,
            unique = true,
            columnDefinition = "VARCHAR(150)"
    )
    private String MerchantRequestID;
    @Column(
            name = "checkoutrequestid",
            nullable = false,
            unique = true,
            columnDefinition = "VARCHAR(150)"
    )
    private String CheckoutRequestID;
    @Column(
            name = "resultcode",
            nullable = false,
            columnDefinition = "VARCHAR(150)"
    )
    private int ResultCode;
    @Column(
            name = "resultdescription",
            nullable = false,
            columnDefinition = "VARCHAR(150)"
    )
    private String ResultDesc;
    @Column(
            name = "transactionreference",
            nullable = false,
            unique = true,
            columnDefinition = "VARCHAR(150)"
    )
    private String transactionReference;
    @Column(
            name = "callbackurl",
            nullable = false,
            columnDefinition = "VARCHAR(150)"
    )
    private String callbackUrl;

    public CallBackMessage createCallbackMessage(TransactionMetaData transactionMetaData) {
        CallBackMessage callBackMessage = new CallBackMessage();
        callBackMessage.setMerchantRequestID(transactionMetaData.getMerchantRequestID());
        callBackMessage.setCheckoutRequestID(transactionMetaData.getCheckoutRequestID());
        callBackMessage.setTransactionReference(transactionMetaData.getMpesaReceiptNumber());
        callBackMessage.setResultCode(Integer.parseInt(transactionMetaData.getResultCode()));
        callBackMessage.setResultDesc(transactionMetaData.getResultDescription());
        callBackMessage.setCallbackUrl(transactionMetaData.getCallBackUrl());
        return callBackMessage;
    }
}

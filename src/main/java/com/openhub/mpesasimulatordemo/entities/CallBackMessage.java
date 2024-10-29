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
            name = "callbackurl",
            nullable = false,
            columnDefinition = "VARCHAR(150)"
    )
    private String callbackUrl;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "merchantrequestid")
    private TransactionData transactionData;

    public CallBackMessage createCallbackMessage(TransactionMessage transactionMessage) {
        TransactionData transactionData = getTransactionData(transactionMessage);

        CallBackMessage message = new CallBackMessage();
        message.setMerchantRequestID(transactionMessage.getMerchantRequestID());
        message.setCheckoutRequestID(transactionMessage.getCheckOutRequestID());
        message.setResultCode(0);
        message.setResultDesc(transactionMessage.getTransactionDescription());
        message.setCallbackUrl(transactionMessage.getCallBackUrl());
        message.setTransactionData(transactionData);
        return message;
    }

    private static TransactionData getTransactionData(TransactionMessage transactionMessage) {
        TransactionData transactionData = new TransactionData();
        transactionData.setMerchantRequestID(transactionMessage.getMerchantRequestID());
        transactionData.setCheckoutRequestID(transactionMessage.getCheckOutRequestID());
        transactionData.setAmount(Double.parseDouble(transactionMessage.getAmount().toString()));
        transactionData.setMpesaReceiptNumber(transactionMessage.getAccountReference());
        transactionData.setTransactionDate(transactionMessage.getTransactionDescription());
        transactionData.setPhoneNumber(transactionMessage.getPhoneNumber());
        return transactionData;
    }
}

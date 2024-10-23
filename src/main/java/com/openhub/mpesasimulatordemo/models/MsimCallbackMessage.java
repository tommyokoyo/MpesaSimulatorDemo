package com.openhub.mpesasimulatordemo.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MsimCallbackMessage {
    private String MerchantRequestID;
    private String CheckoutRequestID;
    private int ResultCode;
    private String ResultDesc;
    private String callbackUrl;
    private List<ItemMetadata> callbackMetadata;

    public MsimCallbackMessage createCallbackMessage(TransactionMessage transactionMessage) {
        ItemMetadata amount = new ItemMetadata();
        amount.setName("Amount");
        amount.setValue(transactionMessage.getAmount().toString());

        ItemMetadata MpesaReceiptNumber = new ItemMetadata();
        MpesaReceiptNumber.setName("MpesaReceiptNumber");
        MpesaReceiptNumber.setValue(transactionMessage.getPartyA());

        ItemMetadata amountTransactionDate = new ItemMetadata();
        amountTransactionDate.setName("AmountTransactionDate");
        amountTransactionDate.setValue(transactionMessage.getTransactionDescription());

        ItemMetadata PhoneNumber = new ItemMetadata();
        PhoneNumber.setName("PhoneNumber");
        PhoneNumber.setValue(transactionMessage.getPhoneNumber());

        callbackMetadata = new ArrayList<ItemMetadata>();

        callbackMetadata.add(amount);
        callbackMetadata.add(MpesaReceiptNumber);
        callbackMetadata.add(amountTransactionDate);
        callbackMetadata.add(PhoneNumber);

        MsimCallbackMessage message = new MsimCallbackMessage();
        message.setMerchantRequestID(transactionMessage.getMerchantRequestID());
        message.setCheckoutRequestID(transactionMessage.getCheckOutRequestID());
        message.setResultCode(0);
        message.setResultDesc(transactionMessage.getTransactionDescription());
        message.setCallbackUrl(transactionMessage.getCallBackUrl());
        message.setCallbackMetadata(callbackMetadata);
        return message;
    }
}

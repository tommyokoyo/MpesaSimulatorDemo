package com.openhub.mpesasimulatordemo.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StkCallbackMessage {
    private String MerchantRequestID;
    private String CheckoutRequestID;
    private int ResultCode;
    private String ResultDesc;
    private List<ItemMetadata> callbackMetadata;

    public StkCallbackMessage createCallbackMessage(String MerchantRequestID, String CheckoutRequestID,
                                            int ResultCode, String ResultDesc, Double transactionAmount,
                                                    String phoneNumber, String AmountTransactionDate,
                                                    String mpesaReceiptNumber) {
        ItemMetadata amount = new ItemMetadata();
        amount.setName("Amount");
        amount.setValue(transactionAmount);

        ItemMetadata MpesaReceiptNumber = new ItemMetadata();
        MpesaReceiptNumber.setName("MpesaReceiptNumber");
        MpesaReceiptNumber.setValue(mpesaReceiptNumber);

        ItemMetadata amountTransactionDate = new ItemMetadata();
        amountTransactionDate.setName("AmountTransactionDate");
        amountTransactionDate.setValue(AmountTransactionDate);

        ItemMetadata PhoneNumber = new ItemMetadata();
        PhoneNumber.setName("PhoneNumber");
        PhoneNumber.setValue(phoneNumber);

        callbackMetadata.add(amount);
        callbackMetadata.add(MpesaReceiptNumber);
        callbackMetadata.add(amountTransactionDate);
        callbackMetadata.add(PhoneNumber);

        StkCallbackMessage message = new StkCallbackMessage();
        message.setMerchantRequestID(MerchantRequestID);
        message.setCheckoutRequestID(CheckoutRequestID);
        message.setResultCode(ResultCode);
        message.setResultDesc(ResultDesc);
        message.setCallbackMetadata(callbackMetadata);
        return message;
    }
}

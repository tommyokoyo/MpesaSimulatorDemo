package com.openhub.mpesasimulatordemo.models;

import com.openhub.mpesasimulatordemo.entities.CallBackMessage;
import com.openhub.mpesasimulatordemo.entities.TransactionMetaData;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MsimStkCallbackRequest {
    private String MerchantRequestID;
    private String CheckoutRequestID;
    private String ResultCode;
    private String ResultDescription;
    private List<ItemMetadata> callbackMetadata;

    public MsimStkCallbackRequest createCallbackRequest(CallBackMessage callbackMessage, TransactionMetaData transactionMetaData) {
        ItemMetadata amountMetadata = new ItemMetadata();
        amountMetadata.setName("Amount");
        amountMetadata.setValue(transactionMetaData.getAmount());

        ItemMetadata referenceMetadata = new ItemMetadata();
        amountMetadata.setName("MpesaReceiptNumber");
        amountMetadata.setValue(transactionMetaData.getAmount());

        ItemMetadata dateMetadata = new ItemMetadata();
        amountMetadata.setName("TransactionDate");
        amountMetadata.setValue(transactionMetaData.getAmount());

        ItemMetadata phoneMetadata = new ItemMetadata();
        amountMetadata.setName("PhoneNumber");
        amountMetadata.setValue(transactionMetaData.getAmount());

        callbackMetadata = new ArrayList<ItemMetadata>();

        callbackMetadata.add(amountMetadata);
        callbackMetadata.add(referenceMetadata);
        callbackMetadata.add(dateMetadata);
        callbackMetadata.add(phoneMetadata);

        MsimStkCallbackRequest msimStkCallbackRequest = new MsimStkCallbackRequest();
        msimStkCallbackRequest.setMerchantRequestID(callbackMessage.getMerchantRequestID());
        msimStkCallbackRequest.setCheckoutRequestID(callbackMessage.getCheckoutRequestID());
        msimStkCallbackRequest.setResultCode(transactionMetaData.getResultCode());
        msimStkCallbackRequest.setResultDescription(transactionMetaData.getResultDescription());
        msimStkCallbackRequest.setCallbackMetadata(callbackMetadata);
        return msimStkCallbackRequest;
    }
}

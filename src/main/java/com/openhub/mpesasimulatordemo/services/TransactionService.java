package com.openhub.mpesasimulatordemo.services;

import com.openhub.mpesasimulatordemo.Utilities.GeneratorComponent;
import com.openhub.mpesasimulatordemo.models.MpesaExpressRequest;
import com.openhub.mpesasimulatordemo.models.ResponseCode;
import com.openhub.mpesasimulatordemo.models.ResponseMessage;
import com.openhub.mpesasimulatordemo.models.StkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final GeneratorComponent generatorComponent;
    private final TransactionQueueProcessorService transactionQueueProcessorService;

    @Autowired
    public TransactionService(GeneratorComponent generatorComponent, TransactionQueueProcessorService transactionQueueProcessorService) {
        this.generatorComponent = generatorComponent;
        this.transactionQueueProcessorService = transactionQueueProcessorService;
    }

    public StkResponse initiateStkPush(MpesaExpressRequest mpesaExpressRequest) {
        if (mpesaExpressRequest == null) {
            // Return Error response
            StkResponse stkResponse = new StkResponse();
            stkResponse.setMerchantRequestID(generatorComponent.MerchantIDGenerator());
            stkResponse.setCheckoutRequestID(generatorComponent.CheckoutRequestIDGenerator());
            stkResponse.setResponseCode(String.valueOf(ResponseCode.FAILED));
            stkResponse.setResponseDescription(ResponseMessage.FAILED.getMessage());
            stkResponse.setCustomerMessage("Error. Could not process request");
            System.out.println("Empty Object: " + stkResponse);
            return stkResponse;
        } else {
            // Send transaction to queue to be processed

            StkResponse stkResponse = new StkResponse();
            stkResponse.setMerchantRequestID(generatorComponent.MerchantIDGenerator());
            stkResponse.setCheckoutRequestID(generatorComponent.CheckoutRequestIDGenerator());
            stkResponse.setResponseCode(String.valueOf(ResponseCode.SUCCESS));
            stkResponse.setResponseDescription(ResponseMessage.SUCCESS.getMessage());
            stkResponse.setCustomerMessage("Success. Request accepted for processing");
            System.out.println("Accepted for Transaction: " + stkResponse);
            return stkResponse;
        }
    }
}

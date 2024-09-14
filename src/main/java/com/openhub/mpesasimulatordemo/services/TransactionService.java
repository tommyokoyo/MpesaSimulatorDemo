package com.openhub.mpesasimulatordemo.services;

import com.openhub.mpesasimulatordemo.Utilities.Generator;
import com.openhub.mpesasimulatordemo.models.MpesaExpressRequest;
import com.openhub.mpesasimulatordemo.models.ResponseCode;
import com.openhub.mpesasimulatordemo.models.ResponseMessage;
import com.openhub.mpesasimulatordemo.models.StkResponse;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    public StkResponse initiateStkPush(MpesaExpressRequest mpesaExpressRequest) {
        if (mpesaExpressRequest == null) {
            // Return Error response
            StkResponse stkResponse = new StkResponse();
            stkResponse.setMerchantRequestID(Generator.MerchantIDGenerator());
            stkResponse.setCheckoutRequestID(Generator.CheckoutRequestIDGenerator());
            stkResponse.setResponseCode(String.valueOf(ResponseCode.FAILED));
            stkResponse.setResponseDescription(ResponseMessage.FAILED.getMessage());
            stkResponse.setCustomerMessage("Error. Could not process request");
            System.out.println("Empty Object: " + stkResponse);
            return stkResponse;
        } else {
            // Initiate STK push to client and send response
            StkResponse stkResponse = new StkResponse();
            stkResponse.setMerchantRequestID(Generator.MerchantIDGenerator());
            stkResponse.setCheckoutRequestID(Generator.CheckoutRequestIDGenerator());
            stkResponse.setResponseCode(String.valueOf(ResponseCode.SUCCESS));
            stkResponse.setResponseDescription(ResponseMessage.SUCCESS.getMessage());
            stkResponse.setCustomerMessage("Success. Request accepted for processing");
            System.out.println("Accepted for Transaction: " + stkResponse);
            return stkResponse;
        }
    }
}

package com.openhub.mpesasimulatordemo.Utilities;

import com.openhub.mpesasimulatordemo.models.ResponseCode;
import com.openhub.mpesasimulatordemo.models.ResponseMessage;
import com.openhub.mpesasimulatordemo.models.StkResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    public static ResponseEntity<StkResponse> buildErrorResponse(HttpStatus status, ResponseCode responseCode, ResponseMessage responseMessage, String message) {
        StkResponse stkResponse = new StkResponse();
        stkResponse.setResponseCode(String.valueOf(responseCode.getResponseCode()));
        stkResponse.setResponseDescription(responseMessage.getMessage());
        stkResponse.setCustomerMessage(message);
        return ResponseEntity.status(status).body(stkResponse);
    }
}

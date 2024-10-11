package com.openhub.mpesasimulatordemo.Utilities;

import com.openhub.mpesasimulatordemo.models.ErrorResponseBuild;
import com.openhub.mpesasimulatordemo.models.ResponseCode;
import com.openhub.mpesasimulatordemo.models.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;

public class ResponseUtil {
    public static ResponseEntity<ErrorResponseBuild> buildErrorResponse(HttpStatus status, ResponseCode responseCode, ResponseMessage responseMessage, String message) {
        ErrorResponseBuild errorResponse = new ErrorResponseBuild();
        errorResponse.setResponseCode(String.valueOf(responseCode.getResponseCode()));
        errorResponse.setResponseDescription(responseMessage.getMessage());
        errorResponse.setCustomerMessage(message);
        return ResponseEntity.status(status).body(errorResponse);
    }
}

package com.openhub.mpesasimulatordemo.controllers;

import com.openhub.mpesasimulatordemo.Utilities.ResponseUtil;
import com.openhub.mpesasimulatordemo.models.MsimStkRequest;
import com.openhub.mpesasimulatordemo.models.ResponseCode;
import com.openhub.mpesasimulatordemo.models.ResponseMessage;
import com.openhub.mpesasimulatordemo.models.MsimStkResponse;
import com.openhub.mpesasimulatordemo.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mpesa-sim/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/mpesa-express")
    public ResponseEntity<?> MpesaExpress(@RequestBody MsimStkRequest msimStkRequest){
        System.out.println("[-] Request received for processing: " + msimStkRequest);
        if (msimStkRequest.getBusinessShortCode() == null || msimStkRequest.getBusinessShortCode().isEmpty()){
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Business short code is required"
            );
        }
        else if (msimStkRequest.getPassword() == null || msimStkRequest.getPassword().isEmpty()){
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Password is required"
            );
        }
        else if (msimStkRequest.getTimestamp() == null || msimStkRequest.getTimestamp().isEmpty()) {
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Timestamp is required"
            );
        }
        else if (msimStkRequest.getTransactionType() == null|| msimStkRequest.getTransactionType().isEmpty()) {
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Transaction Type is required"
            );
        }
        else if (msimStkRequest.getAmount() == null || msimStkRequest.getAmount().isEmpty()){
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Amount is required"
            );
        }
        else if (msimStkRequest.getPartyA() == null || msimStkRequest.getPartyA().isEmpty()){
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Party A is required"
            );
        }
        else if (msimStkRequest.getPartyB() == null || msimStkRequest.getPartyB().isEmpty()){
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Party B is required"
            );
        }
        else if (msimStkRequest.getPhoneNumber() == null || msimStkRequest.getPhoneNumber().isEmpty()){
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Phone Number is required"
            );
        }
        else if (msimStkRequest.getCallBackURL() == null || msimStkRequest.getCallBackURL().isEmpty()){
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Callback URL is required"
            );
        }
        else if (msimStkRequest.getAccountReference() == null || msimStkRequest.getAccountReference().isEmpty()){
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Account Reference is required"
            );
        }
        else if (msimStkRequest.getTransactionDesc() == null || msimStkRequest.getTransactionDesc().isEmpty()){
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Transaction Description is required"
            );
        }
        // Mimic Transaction initiation and return response
        MsimStkResponse msimStkResponse = transactionService.initiateStkPush(msimStkRequest);
         return ResponseEntity.status(HttpStatus.OK).body(msimStkResponse);
    }
}

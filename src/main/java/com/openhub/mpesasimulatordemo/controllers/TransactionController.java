package com.openhub.mpesasimulatordemo.controllers;

import com.openhub.mpesasimulatordemo.Utilities.ResponseUtil;
import com.openhub.mpesasimulatordemo.models.MpesaExpressRequest;
import com.openhub.mpesasimulatordemo.models.ResponseCode;
import com.openhub.mpesasimulatordemo.models.ResponseMessage;
import com.openhub.mpesasimulatordemo.models.StkResponse;
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
    public ResponseEntity<?> MpesaExpress(@RequestBody MpesaExpressRequest mpesaExpressRequest){
        System.out.println(mpesaExpressRequest);
        if (mpesaExpressRequest.getBusinessShortCode() == null || mpesaExpressRequest.getBusinessShortCode().isEmpty()){
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Business short code is required"
            );
        } else if (mpesaExpressRequest.getPassword() == null || mpesaExpressRequest.getPassword().isEmpty()){
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Password is required"
            );
        } else if (mpesaExpressRequest.getTimestamp() == null || mpesaExpressRequest.getTimestamp().isEmpty()) {
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Timestamp is required"
            );
        }else if (mpesaExpressRequest.getTransactionType() == null) {
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Transaction Type is required"
            );
        } else if (mpesaExpressRequest.getAmount() == null || mpesaExpressRequest.getAmount().isEmpty()){
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Amount is required"
            );
        } else if (mpesaExpressRequest.getPartyA() == null || mpesaExpressRequest.getPartyA().isEmpty()){
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Party A is required"
            );
        }else if (mpesaExpressRequest.getPartyB() == null || mpesaExpressRequest.getPartyB().isEmpty()){
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Party B is required"
            );
        } else if (mpesaExpressRequest.getPhoneNumber() == null || mpesaExpressRequest.getPhoneNumber().isEmpty()){
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Phone Number is required"
            );
        }else if (mpesaExpressRequest.getCallBackURL() == null || mpesaExpressRequest.getCallBackURL().isEmpty()){
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Callback URL is required"
            );
        }else if (mpesaExpressRequest.getAccountReference() == null || mpesaExpressRequest.getAccountReference().isEmpty()){
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Account Reference is required"
            );
        }else if (mpesaExpressRequest.getTransactionDesc() == null || mpesaExpressRequest.getTransactionDesc().isEmpty()){
            return ResponseUtil.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.FAILED,
                    ResponseMessage.INVALID,
                    "Transaction Description is required"
            );
        }
        // Mimic Transaction initiation and return response
        StkResponse stkResponse = transactionService.initiateStkPush(mpesaExpressRequest);
         return ResponseEntity.status(HttpStatus.OK).body(stkResponse);
    }
}

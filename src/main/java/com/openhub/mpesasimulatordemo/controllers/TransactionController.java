package com.openhub.mpesasimulatordemo.controllers;

import com.openhub.mpesasimulatordemo.models.MpesaExpressRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mpesa-sim/transaction")
public class TransactionController {
    @PostMapping("/mpesa-express")
    public ResponseEntity<?> MpesaExpress(@RequestBody MpesaExpressRequest mpesaExpressRequest){
         return ResponseEntity.ok(mpesaExpressRequest);
    }
}

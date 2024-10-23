package com.openhub.mpesasimulatordemo.models;

import lombok.Data;

import java.util.List;

@Data
public class MsimStkCallbackRequest {
    private String MerchantRequestID;
    private String CheckoutRequestID;
    private String ResultCode;
    private String ResultDescription;
    private List<ItemMetadata> callbackMetadata;
}

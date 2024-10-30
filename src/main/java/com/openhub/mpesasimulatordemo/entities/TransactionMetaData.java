package com.openhub.mpesasimulatordemo.entities;

import com.openhub.mpesasimulatordemo.models.ItemMetadata;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "transactions_metadata")
public class TransactionMetaData {
    @Id
    @Column(name = "merchantrequestid", nullable = false, length = 150)
    private String MerchantRequestID;
    @Column(name = "checkoutrequestid", nullable = false, length = 150)
    private String CheckoutRequestID;
    @Column(name = "phonenumber", nullable = false, length = 150)
    private String phoneNumber;
    @Column(name = "amount", nullable = false, length = 150)
    private Double amount;
    @Column(name = "mpesareceiptnumber", nullable = false, length = 150)
    private String mpesaReceiptNumber;
    @Column(name = "transactiondate", nullable = false, length = 150)
    private String transactionDate;
    @Column(name = "resultcode", nullable = false, length = 150)
    private String resultCode;
    @Column(name = "resultdescription",nullable = false, length = 150)
    private String resultDescription;
    @Column(name = "callbackurl",nullable = false, length = 150)
    private String callBackUrl;
}

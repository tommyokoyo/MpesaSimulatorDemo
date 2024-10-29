package com.openhub.mpesasimulatordemo.entities;

import com.openhub.mpesasimulatordemo.models.ItemMetadata;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "transaction_data")
public class TransactionData {
    @Id
    @Column(name = "merchantrequestid", nullable = false, length = 150)
    private String MerchantRequestID;
    @Column(name = "checkoutrequestid", nullable = false, length = 150)
    private String CheckoutRequestID;
    @Column(name = "amount", nullable = false, length = 150)
    private double amount;
    @Column(name = "mpesareceiptnumber", nullable = false, length = 150)
    private String mpesaReceiptNumber;
    @Column(name = "transactiondate", nullable = false, length = 150)
    private String transactionDate;
    @Column(name = "phonenumber", nullable = false, length = 150)
    private String phoneNumber;
    @OneToOne(mappedBy = "transactionData", cascade = CascadeType.ALL)
    private CallBackMessage callBackMessage;
}

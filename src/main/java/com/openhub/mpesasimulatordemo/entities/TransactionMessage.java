package com.openhub.mpesasimulatordemo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(
        name = "transaction_message"
)
public class TransactionMessage {
    @Id
    @Column(
            name = "merchantrequestid",
            nullable = false,
            unique = true,
            columnDefinition = "VARCHAR(150)"
    )
    private String merchantRequestID;
    @Column(
            name = "checkoutrequestid",
            nullable = false,
            unique = true,
            columnDefinition = "VARCHAR(150)"
    )
    private String checkOutRequestID;
    @Column(
            name = "transactiontype",
            nullable = false,
            columnDefinition = "VARCHAR(150)"
    )
    private String transactionType;
    @Column(
            name = "amount",
            nullable = false,
            columnDefinition = "VARCHAR(150)"
    )
    private Double amount;
    @Column(
            name = "partya",
            nullable = false,
            columnDefinition = "VARCHAR(150)"
    )
    private String partyA;
    @Column(
            name = "partyb",
            nullable = false,
            columnDefinition = "VARCHAR(150)"
    )
    private String partyB;
    @Column(
            name = "phonenumber",
            nullable = false,
            columnDefinition = "VARCHAR(150)"
    )
    private String phoneNumber;
    @Column(
            name = "callbackurl",
            nullable = false,
            columnDefinition = "VARCHAR(150)"
    )
    private String callBackUrl;
    @Column(
            name = "accountreference",
            nullable = false,
            columnDefinition = "VARCHAR(150)"
    )
    private String accountReference;
    @Column(
            name = "transactiondescription",
            nullable = false,
            columnDefinition = "VARCHAR(150)"
    )
    private String transactionDescription;
}

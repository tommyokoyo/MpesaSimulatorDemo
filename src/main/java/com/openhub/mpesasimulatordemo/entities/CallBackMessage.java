package com.openhub.mpesasimulatordemo.entities;

import com.openhub.mpesasimulatordemo.entities.ItemMetadata;
import com.openhub.mpesasimulatordemo.models.MsimCallbackMessage;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(
        name = "callbackmessage",
        uniqueConstraints = {
                @UniqueConstraint(name = "merchantrequestid_constraint", columnNames = "merchantrequestid"),
                @UniqueConstraint(name = "checkoutrequestid_constraint", columnNames = "checkoutrequestid"),
        }
)
public class CallBackMessage {
    @Id
    @Column(
            name = "merchantrequestid",
            nullable = false,
            unique = true,
            columnDefinition = "VARCHAR(150)"
    )
    private String MerchantRequestID;
    @Column(
            name = "checkoutrequestid",
            nullable = false,
            unique = true,
            columnDefinition = "VARCHAR(150)"
    )
    private String CheckoutRequestID;
    @Column(
            name = "resultcode",
            nullable = false,
            columnDefinition = "VARCHAR(150)"
    )
    private int ResultCode;
    @Column(
            name = "resultdescription",
            nullable = false,
            columnDefinition = "VARCHAR(150)"
    )
    private String ResultDesc;
    @Column(
            name = "callbackurl",
            nullable = false,
            columnDefinition = "VARCHAR(150)"
    )
    private String callbackUrl;

    @ElementCollection
    @CollectionTable(
            name = "callbackmessage_callbackmetadata",
            joinColumns = @JoinColumn(name = "merchantrequestid")
    )
    @Column(
            name = "callbackmetadata",
            nullable = false,
            columnDefinition = "VARCHAR(150)"
    )
    private List<ItemMetadata> callbackMetadata;

    public CallBackMessage createCallbackMessage(TransactionMessage transactionMessage) {
        ItemMetadata amount = new ItemMetadata();
        amount.setName("Amount");
        amount.setValue(transactionMessage.getAmount().toString());

        ItemMetadata MpesaReceiptNumber = new ItemMetadata();
        MpesaReceiptNumber.setName("MpesaReceiptNumber");
        MpesaReceiptNumber.setValue(transactionMessage.getPartyA());

        ItemMetadata amountTransactionDate = new ItemMetadata();
        amountTransactionDate.setName("AmountTransactionDate");
        amountTransactionDate.setValue(transactionMessage.getTransactionDescription());

        ItemMetadata PhoneNumber = new ItemMetadata();
        PhoneNumber.setName("PhoneNumber");
        PhoneNumber.setValue(transactionMessage.getPhoneNumber());

        callbackMetadata = new ArrayList<>();

        callbackMetadata.add(amount);
        callbackMetadata.add(MpesaReceiptNumber);
        callbackMetadata.add(amountTransactionDate);
        callbackMetadata.add(PhoneNumber);

        CallBackMessage message = new CallBackMessage();
        message.setMerchantRequestID(transactionMessage.getMerchantRequestID());
        message.setCheckoutRequestID(transactionMessage.getCheckOutRequestID());
        message.setResultCode(0);
        message.setResultDesc(transactionMessage.getTransactionDescription());
        message.setCallbackUrl(transactionMessage.getCallBackUrl());
        message.setCallbackMetadata(callbackMetadata);
        return message;
    }
}

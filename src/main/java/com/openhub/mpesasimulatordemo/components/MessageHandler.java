package com.openhub.mpesasimulatordemo.components;

import com.openhub.mpesasimulatordemo.Utilities.GeneratorComponent;
import com.openhub.mpesasimulatordemo.entities.TransactionMessage;
import com.openhub.mpesasimulatordemo.models.*;
import com.openhub.mpesasimulatordemo.repository.TransactionRepository;
import com.openhub.mpesasimulatordemo.services.QueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This component provides methods for message handling, translation and adding to queue.
 * <p>
 *
 * @author Thomas Okoyo
 * @version 1.0
 */
@Slf4j
@Component
public class MessageHandler {
    private final GeneratorComponent generatorComponent;
    private final QueueService queueService;
    private final TransactionRepository transactionRepository;

    private static final String TRANSACTION_QUEUE = "transactionQueue";

    @Autowired
    public MessageHandler(GeneratorComponent generatorComponent, QueueService queueService, TransactionRepository transactionRepository) {
        this.generatorComponent = generatorComponent;
        this.queueService = queueService;
        this.transactionRepository = transactionRepository;
    }

    /**
     * This method returns an object of MsimStkResponse
     *
     * @return MsimStkResponse object
     */
    private MsimStkResponse StkResponse() {
        return new MsimStkResponse();
    }

    /**
     * This method returns an error response of type MsimStkResponse
     *
     * @return MsimStkResponse Object
     */
    public MsimStkResponse StkErrorResponse() {
        MsimStkResponse msimStkResponse = StkResponse();
        msimStkResponse.setMerchantRequestID(generatorComponent.MerchantIDGenerator());
        msimStkResponse.setCheckoutRequestID(generatorComponent.CheckoutRequestIDGenerator());
        msimStkResponse.setResponseCode(String.valueOf(ResponseCode.FAILED));
        msimStkResponse.setResponseDescription(ResponseMessage.FAILED.getMessage());
        msimStkResponse.setCustomerMessage("Error. Could not process request");
        return msimStkResponse;
    }

    /**
     * This method calls the createTransaction method
     *
     * @param msimStkRequest request object
     *
     * @return MsimStkResponse Object
     */
    public MsimStkResponse handleTransaction(MsimStkRequest msimStkRequest) {
        // Create and log the transaction
        return createTransaction(msimStkRequest);
    }

    /**
     * This method initializes the transaction and adds it in the queue
     *
     * @return MsimStkResponse Object
     */
    private MsimStkResponse createTransaction(MsimStkRequest msimStkRequest) {
        log.info("Processing transaction for Phone Number: {}", msimStkRequest.getPhoneNumber());
        TransactionMessage transactionMessage = new TransactionMessage();
        transactionMessage.setMerchantRequestID(generatorComponent.MerchantIDGenerator());
        transactionMessage.setCheckOutRequestID(generatorComponent.CheckoutRequestIDGenerator());
        transactionMessage.setTransactionType(msimStkRequest.getTransactionType());
        transactionMessage.setPartyA(msimStkRequest.getPartyA());
        transactionMessage.setPartyB(msimStkRequest.getPartyB());
        transactionMessage.setPhoneNumber(msimStkRequest.getPhoneNumber());
        transactionMessage.setAmount(Double.valueOf(msimStkRequest.getAmount()));
        transactionMessage.setCallBackUrl(msimStkRequest.getCallBackURL());
        transactionMessage.setAccountReference(msimStkRequest.getAccountReference());
        transactionMessage.setTransactionDescription(msimStkRequest.getTransactionDesc());
        log.info("Transaction created with MerchantRequestID: {}", transactionMessage.getMerchantRequestID());
        // Add transaction to queue for processing
        try {
            queueService.addTransactionToQueue(TRANSACTION_QUEUE,transactionMessage);
            try{
                // write transaction to Database
                transactionRepository.save(transactionMessage);
                MsimStkResponse msimStkResponse = StkResponse();
                msimStkResponse.setMerchantRequestID(transactionMessage.getMerchantRequestID());
                msimStkResponse.setCheckoutRequestID(transactionMessage.getCheckOutRequestID());
                msimStkResponse.setResponseCode(String.valueOf(ResponseCode.SUCCESS));
                msimStkResponse.setResponseDescription(ResponseMessage.SUCCESS.getMessage());
                msimStkResponse.setCustomerMessage("Success. Request accepted for processing");
                log.info("Transaction with MerchantRequestID {} added to Database", transactionMessage.getMerchantRequestID());
                return msimStkResponse;
            } catch (Exception e) {
                log.error("Failed to add Transaction with MerchantRequestID {} to the database", transactionMessage.getMerchantRequestID());
                log.error(e.getMessage());
                MsimStkResponse msimStkResponse = new MsimStkResponse();
                msimStkResponse.setMerchantRequestID(transactionMessage.getMerchantRequestID());
                msimStkResponse.setCheckoutRequestID(transactionMessage.getCheckOutRequestID());
                msimStkResponse.setResponseCode(String.valueOf(ResponseCode.SUCCESS));
                msimStkResponse.setResponseDescription(ResponseMessage.SUCCESS.getMessage());
                msimStkResponse.setCustomerMessage("Success. Request accepted for processing");
                log.info("Sending success Response for request with MerchantRequestID: {}", transactionMessage.getMerchantRequestID());
                return msimStkResponse;
            }
        } catch (Exception e) {
            log.error("Failed to add Transaction with MerchantRequestID {} to the queue", transactionMessage.getMerchantRequestID());
            MsimStkResponse msimStkResponse = new MsimStkResponse();
            msimStkResponse.setMerchantRequestID(transactionMessage.getMerchantRequestID());
            msimStkResponse.setCheckoutRequestID(transactionMessage.getCheckOutRequestID());
            msimStkResponse.setResponseCode(String.valueOf(ResponseCode.FAILED));
            msimStkResponse.setResponseDescription(ResponseMessage.FAILED.getMessage());
            msimStkResponse.setCustomerMessage("Error. Could not process request");
            return msimStkResponse;
        }
    }
}

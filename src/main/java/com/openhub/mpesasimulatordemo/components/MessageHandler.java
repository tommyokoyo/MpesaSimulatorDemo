package com.openhub.mpesasimulatordemo.components;

import com.openhub.mpesasimulatordemo.Utilities.GeneratorComponent;
import com.openhub.mpesasimulatordemo.configuration.RabbitMQConfiguration;
import com.openhub.mpesasimulatordemo.entities.TransactionMessage;
import com.openhub.mpesasimulatordemo.models.*;
import com.openhub.mpesasimulatordemo.repository.TransactionRepository;
import com.openhub.mpesasimulatordemo.services.QueueService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This component provides methods for message handling, translation and adding to queue.
 * <p>
 *
 * @author Thomas Okoyo
 * @version 1.0
 */
@Component
public class MessageHandler {
    private final GeneratorComponent generatorComponent;
    private final QueueService queueService;
    private final TransactionRepository transactionRepository;

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
    public MsimStkResponse handleTranscation(MsimStkRequest msimStkRequest) {
        // Create and log the transaction
        return createTransaction(msimStkRequest);
    }

    /**
     * This method initializes the transaction and adds it in the queue
     *
     * @return MsimStkResponse Object
     */
    private MsimStkResponse createTransaction(MsimStkRequest msimStkRequest) {
        System.out.println("Received payload: " + msimStkRequest);
        TransactionMessage transactionMessage = new TransactionMessage();
        transactionMessage.setTransactionType(msimStkRequest.getTransactionType());
        transactionMessage.setAmount(Double.valueOf(msimStkRequest.getAmount()));
        transactionMessage.setPartyA(msimStkRequest.getPartyA());
        transactionMessage.setPartyB(msimStkRequest.getPartyB());
        transactionMessage.setPhoneNumber(msimStkRequest.getPhoneNumber());
        transactionMessage.setCallBackUrl(msimStkRequest.getCallBackURL());
        transactionMessage.setAccountReference(msimStkRequest.getAccountReference());
        transactionMessage.setTransactionDescription(msimStkRequest.getTransactionDesc());
        transactionMessage.setMerchantRequestID(generatorComponent.MerchantIDGenerator());
        transactionMessage.setCheckOutRequestID(generatorComponent.CheckoutRequestIDGenerator());

        // Add transaction to queue for processing
        try {
            queueService.addTransactionToQueue("transactionQueue",transactionMessage);
            try{
                // write transaction to Database
                transactionRepository.save(transactionMessage);
                MsimStkResponse msimStkResponse = StkResponse();
                msimStkResponse.setMerchantRequestID(transactionMessage.getMerchantRequestID());
                msimStkResponse.setCheckoutRequestID(transactionMessage.getCheckOutRequestID());
                msimStkResponse.setResponseCode(String.valueOf(ResponseCode.SUCCESS));
                msimStkResponse.setResponseDescription(ResponseMessage.SUCCESS.getMessage());
                msimStkResponse.setCustomerMessage("Success. Request accepted for processing");
                System.out.println("[-] Transaction "+ transactionMessage.getCheckOutRequestID() +" added to the queue");
                return msimStkResponse;
            } catch (Exception e) {
                System.out.println("[-] Error adding transaction to Database"+ transactionMessage.getCheckOutRequestID() +"to database");
                System.out.println("[-] Exception thrown: "+ e.getMessage());
                MsimStkResponse msimStkResponse = new MsimStkResponse();
                msimStkResponse.setMerchantRequestID(transactionMessage.getMerchantRequestID());
                msimStkResponse.setCheckoutRequestID(transactionMessage.getCheckOutRequestID());
                msimStkResponse.setResponseCode(String.valueOf(ResponseCode.SUCCESS));
                msimStkResponse.setResponseDescription(ResponseMessage.SUCCESS.getMessage());
                msimStkResponse.setCustomerMessage("Success. Request accepted for processing");
                return msimStkResponse;
            }

        } catch (Exception e) {
            System.out.println("[-] Error adding transaction "+ transactionMessage.getCheckOutRequestID() +"to queue");
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

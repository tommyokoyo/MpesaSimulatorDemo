package com.openhub.mpesasimulatordemo.services;

import com.openhub.mpesasimulatordemo.configuration.RabbitMQConfiguration;
import com.openhub.mpesasimulatordemo.entities.TransactionMessage;
import com.openhub.mpesasimulatordemo.models.MsimCallbackMessage;
import com.openhub.mpesasimulatordemo.entities.CallBackMessage;
import com.openhub.mpesasimulatordemo.models.MsimStkCallbackRequest;
import com.openhub.mpesasimulatordemo.models.MsimStkCallbackResponse;
import com.openhub.mpesasimulatordemo.repository.CallbackRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueueProcessors {
    private final RabbitTemplate rabbitTemplate;
    private final CallbackService callbackService;
    private final CallbackRepository callbackRepository;

    @Autowired
    public QueueProcessors(RabbitTemplate rabbitTemplate, CallbackService callbackService, CallbackRepository callbackRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.callbackService = callbackService;
        this.callbackRepository = callbackRepository;
    }

    @RabbitListener(queues = RabbitMQConfiguration.TRANSACTION_QUEUE)
    public void processTransaction(TransactionMessage transactionMessage) {
        System.out.println("[-] Received transaction for processing with CheckoutRequestID: " + transactionMessage.getCheckOutRequestID());
        CallBackMessage msimcallbackMessage = new CallBackMessage();
        msimcallbackMessage = msimcallbackMessage.createCallbackMessage(transactionMessage);
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfiguration.CALLBACK_QUEUE, msimcallbackMessage);
            System.out.println("[-] Transaction : " + transactionMessage.getCheckOutRequestID()+ " added to the callback queue");
            try {
                callbackRepository.save(msimcallbackMessage);
            } catch (Exception e) {
                System.out.println("[-] Error writing callback message: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("[-] Error writing to call back queue: " + e.getMessage());
        }
    }

    @RabbitListener(queues = RabbitMQConfiguration.CALLBACK_QUEUE)
    public void processCallback(MsimCallbackMessage msimCallbackMessage) {
        System.out.println("[-] Received Callback for processing with CheckoutRequestID: " + msimCallbackMessage.getCheckoutRequestID());
        MsimStkCallbackRequest msimStkCallbackRequest = new MsimStkCallbackRequest();
        msimStkCallbackRequest.setMerchantRequestID(msimCallbackMessage.getCheckoutRequestID());
        msimStkCallbackRequest.setCheckoutRequestID(msimCallbackMessage.getCheckoutRequestID());
        msimStkCallbackRequest.setResultCode(String.valueOf(msimCallbackMessage.getResultCode()));
        msimStkCallbackRequest.setResultDescription(msimCallbackMessage.getResultDesc());
        msimStkCallbackRequest.setCallbackMetadata(msimCallbackMessage.getCallbackMetadata());
        System.out.println("[-] Full callback request: " + msimStkCallbackRequest);
        try {
            // Sleep for 5 seconds to imitate the request
            Thread.sleep(5000);
            MsimStkCallbackResponse msimStkCallbackResponse= callbackService.stkCallback(msimStkCallbackRequest, msimCallbackMessage.getCallbackUrl()).block();
            System.out.println("[-] Callback Response : " + msimStkCallbackResponse);
        } catch (Exception e) {
            System.out.println("[-] Error Sending callback queue: " + e.getMessage());
        }
    }
}

package com.openhub.mpesasimulatordemo.services;

import com.openhub.mpesasimulatordemo.configuration.RabbitMQConfiguration;
import com.openhub.mpesasimulatordemo.entities.TransactionMessage;
import com.openhub.mpesasimulatordemo.models.MsimCallbackMessage;
import com.openhub.mpesasimulatordemo.entities.CallBackMessage;
import com.openhub.mpesasimulatordemo.models.MsimStkCallbackRequest;
import com.openhub.mpesasimulatordemo.models.MsimStkCallbackResponse;
import com.openhub.mpesasimulatordemo.repository.CallbackRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Service
public class QueueProcessors {
    private final CallbackService callbackService;
    private final CallbackRepository callbackRepository;
    private final QueueService queueService;

    private static final String TRANSACTION_QUEUE = "transactionQueue";
    private static final String CALLBACK_QUEUE = "callbackQueue";

    @Autowired
    public QueueProcessors(CallbackService callbackService, CallbackRepository callbackRepository, QueueService queueService) {
        this.callbackService = callbackService;
        this.callbackRepository = callbackRepository;
        this.queueService = queueService;
    }

    @PostConstruct
    public void startQueueProcessor() {
        new Thread(this::processTransaction).start();
    }

    private void processTransaction() {
        while (true) {
            try {
                if (!queueService.isQueueEmpty(TRANSACTION_QUEUE)) {
                    TransactionMessage transactionMessage = queueService.getTransactionFromQueue(TRANSACTION_QUEUE);

                    if (transactionMessage != null) {
                        log.info("Transaction Message received: {}", transactionMessage);

                        CallBackMessage msimcallbackMessage = new CallBackMessage();
                        msimcallbackMessage = msimcallbackMessage.createCallbackMessage(transactionMessage);

                        try {
                            log.info("Processing transaction with getMerchantRequestID: {}", transactionMessage.getMerchantRequestID());
                            queueService.addCallbackToQueue(CALLBACK_QUEUE, msimcallbackMessage);
                            log.info("Transaction {} added to the callback queue", transactionMessage.getMerchantRequestID());
                            try {
                                callbackRepository.save(msimcallbackMessage);
                            } catch (Exception e) {
                                log.error("Error writing transaction {} to the database queue", transactionMessage.getMerchantRequestID());
                            }
                        } catch (Exception e) {
                            log.error("Error writing transaction {} to the callback queue", transactionMessage.getMerchantRequestID());
                        }
                    } else {
                        Thread.sleep(1000);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Error processing transaction: {}", e.getMessage());
            }
        }
    }

    private void processCallback(MsimCallbackMessage msimCallbackMessage) {
        log.info("Received Callback {} for processing", msimCallbackMessage.getMerchantRequestID());
        MsimStkCallbackRequest msimStkCallbackRequest = new MsimStkCallbackRequest();
        msimStkCallbackRequest.setMerchantRequestID(msimCallbackMessage.getCheckoutRequestID());
        msimStkCallbackRequest.setCheckoutRequestID(msimCallbackMessage.getCheckoutRequestID());
        msimStkCallbackRequest.setResultCode(String.valueOf(msimCallbackMessage.getResultCode()));
        msimStkCallbackRequest.setResultDescription(msimCallbackMessage.getResultDesc());
        msimStkCallbackRequest.setCallbackMetadata(msimCallbackMessage.getCallbackMetadata());
        log.info("Callback {}", msimCallbackMessage);
        try {
            // Sleep for 5 seconds to imitate the request
            Thread.sleep(5000);
            MsimStkCallbackResponse msimStkCallbackResponse= callbackService.stkCallback(msimStkCallbackRequest, msimCallbackMessage.getCallbackUrl()).block();
            log.info("Callback Response: {}", msimStkCallbackResponse);
        } catch (Exception e) {
            log.warn("Error Sending callback queue {}", e.getMessage());
        }
    }
}

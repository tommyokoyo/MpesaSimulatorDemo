package com.openhub.mpesasimulatordemo.services;

import com.openhub.mpesasimulatordemo.Utilities.GeneratorComponent;
import com.openhub.mpesasimulatordemo.entities.TransactionMessage;
import com.openhub.mpesasimulatordemo.entities.TransactionMetaData;
import com.openhub.mpesasimulatordemo.entities.CallBackMessage;
import com.openhub.mpesasimulatordemo.models.MsimStkCallbackRequest;
import com.openhub.mpesasimulatordemo.models.MsimStkCallbackResponse;
import com.openhub.mpesasimulatordemo.repository.CallbackRepository;
import com.openhub.mpesasimulatordemo.repository.TransactionMetaDataRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class QueueProcessors {
    private final CallbackService callbackService;
    private final TransactionMetaDataRepository transactionMetaDataRepository;
    private final QueueService queueService;
    private final GeneratorComponent generatorComponent;
    private final CallbackRepository callbackRepository;

    private static final String TRANSACTION_QUEUE = "transactionQueue";
    private static final String CALLBACK_QUEUE = "callbackQueue";

    @Autowired
    public QueueProcessors(CallbackService callbackService, CallbackRepository callbackRepository, QueueService queueService, GeneratorComponent generatorComponent, TransactionMetaDataRepository transactionMetaDataRepository) {
        this.callbackService = callbackService;
        this.callbackRepository = callbackRepository;
        this.queueService = queueService;
        this.generatorComponent = generatorComponent;
        this.transactionMetaDataRepository = transactionMetaDataRepository;
    }

    @PostConstruct
    public void startQueueProcessor() {
        new Thread(this::processTransaction).start();
        new Thread(this::processCallback).start();
    }

    private void processTransaction() {
        while (true) {
            try {
                if (!queueService.checkQueue(TRANSACTION_QUEUE)) {
                    TransactionMessage transactionMessage = queueService.getTransactionFromQueue(TRANSACTION_QUEUE);

                    if (transactionMessage != null) {
                        log.info("Processing transaction with MerchantRequestID {}", transactionMessage.getMerchantRequestID());
                        TransactionMetaData transactionMetaData = new TransactionMetaData();
                        transactionMetaData.setMerchantRequestID(transactionMessage.getMerchantRequestID());
                        transactionMetaData.setCheckoutRequestID(transactionMessage.getCheckOutRequestID());
                        transactionMetaData.setPhoneNumber(transactionMessage.getPhoneNumber());
                        transactionMetaData.setAmount(transactionMessage.getAmount());
                        transactionMetaData.setMpesaReceiptNumber(generatorComponent.transactionRefGenerator());
                        transactionMetaData.setTransactionDate(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                        transactionMetaData.setResultCode("0");
                        transactionMetaData.setResultDescription("Success");
                        transactionMetaData.setCallBackUrl(transactionMessage.getCallBackUrl());

                        CallBackMessage callBackMessage = new CallBackMessage();
                        callBackMessage = callBackMessage.createCallbackMessage(transactionMetaData);

                        try {
                            log.info("Transaction with MerchantRequestID {} was processed successfully with reference {}", transactionMetaData.getMerchantRequestID(), transactionMetaData.getMpesaReceiptNumber());
                            transactionMetaDataRepository.save(transactionMetaData);
                            log.info("Transaction with reference {} written to database", transactionMetaData.getMpesaReceiptNumber());
                            try {
                                // send to callback queue for processing
                                queueService.addCallbackToQueue(CALLBACK_QUEUE, callBackMessage);
                            } catch (Exception e) {
                                log.error("Error writing transaction {} to the callback queue", transactionMessage.getMerchantRequestID());
                            }
                        } catch (Exception e) {
                            log.error("Error writing Transaction with Reference {} to the database", transactionMetaData.getMpesaReceiptNumber());
                        }
                    } else {
                        //noinspection BusyWait
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

    private void processCallback() {
        while (true) {
            try {
                if (!queueService.checkQueue(CALLBACK_QUEUE)) {
                    CallBackMessage callBackMessage = queueService.getCallbackFromQueue(CALLBACK_QUEUE);
                    if (callBackMessage != null) {
                        log.info("Processing Callback message with Transaction Reference {} for processing", callBackMessage.getTransactionReference());
                        try {
                            Optional<TransactionMetaData> transactionMetaData = transactionMetaDataRepository.findByTransactionId(callBackMessage.getTransactionReference());
                            log.info("pulled record {}", transactionMetaData);
                            if (transactionMetaData!= null && transactionMetaData.isPresent()) {
                                MsimStkCallbackRequest msimStkCallbackRequest = new MsimStkCallbackRequest().createCallbackRequest(callBackMessage, transactionMetaData.get());
                                try {
                                    log.info("Writing callback Message with reference {} to the Database", callBackMessage.getTransactionReference());
                                    log.info("to save callback message {}", callBackMessage);
                                    callbackRepository.save(callBackMessage);
                                    try {
                                        MsimStkCallbackResponse msimStkCallbackResponse= callbackService.stkCallback(msimStkCallbackRequest, transactionMetaData.get().getCallBackUrl()).block();
                                        log.info("Callback Response for callback with reference{} is {}", callBackMessage.getTransactionReference(), msimStkCallbackResponse);
                                        //noinspection BusyWait
                                        Thread.sleep(5000);
                                    } catch (Exception e) {
                                        log.warn("Error Sending callback to client {}", e.getMessage());
                                    }
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                    log.error("Error writing callback message with reference {} to database", callBackMessage.getTransactionReference());
                                }
                            } else {
                                log.error("Transaction Metadata for transaction with reference {} is empty for processing", callBackMessage.getTransactionReference());
                            }
                        } catch (Exception e) {
                            log.error("Error fetching Transaction Metadata from the database: {}", e.getMessage());
                        }
                    } else {
                        //noinspection BusyWait
                        Thread.sleep(1000);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Error processing Callback: {}", e.getMessage());
            }
        }
    }
}

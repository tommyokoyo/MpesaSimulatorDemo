package com.openhub.mpesasimulatordemo.services;

import com.openhub.mpesasimulatordemo.components.MessageHandler;
import com.openhub.mpesasimulatordemo.models.MsimStkRequest;
import com.openhub.mpesasimulatordemo.models.MsimStkResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service class contains method to initiate stkPush
 */
@Slf4j
@Service
public class TransactionService {
    private final MessageHandler messageHandler;

    @Autowired
    public TransactionService(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public MsimStkResponse initiateStkPush(MsimStkRequest msimStkRequest) {
        if (msimStkRequest == null) {
            log.error("MsimStkRequest Object is Null");
            return messageHandler.StkErrorResponse();
        } else {
            // Send transaction to MessageHandler for processing
            return messageHandler.handleTransaction(msimStkRequest);
        }
    }
}

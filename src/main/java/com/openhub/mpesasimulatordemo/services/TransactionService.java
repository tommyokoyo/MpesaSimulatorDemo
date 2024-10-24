package com.openhub.mpesasimulatordemo.services;

import com.openhub.mpesasimulatordemo.Utilities.GeneratorComponent;
import com.openhub.mpesasimulatordemo.components.MessageHandler;
import com.openhub.mpesasimulatordemo.models.MsimStkRequest;
import com.openhub.mpesasimulatordemo.models.ResponseCode;
import com.openhub.mpesasimulatordemo.models.ResponseMessage;
import com.openhub.mpesasimulatordemo.models.MsimStkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service class contains method to initiate stkPush
 */
@Service
public class TransactionService {
    private final MessageHandler messageHandler;

    @Autowired
    public TransactionService(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public MsimStkResponse initiateStkPush(MsimStkRequest msimStkRequest) {
        if (msimStkRequest == null) {
            return messageHandler.StkErrorResponse();
        } else {
            // Send transaction to MessageHandler for processing
            return messageHandler.handleTranscation(msimStkRequest);
        }
    }
}

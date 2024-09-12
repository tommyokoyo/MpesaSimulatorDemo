package com.openhub.mpesasimulatordemo.services;

import com.openhub.mpesasimulatordemo.Utilities.RabbitMQConfig;
import com.openhub.mpesasimulatordemo.models.MpesaCallbackMessage;
import com.openhub.mpesasimulatordemo.models.TransactionMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MpesaServices {
    private final RabbitTemplate rabbitTemplate;

    public MpesaServices(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.TRANSACTION_QUEUE)
    public void lipaNaMpesaDemo(TransactionMessage transactionMessage) {
        System.out.println("Transaction " + transactionMessage.getTransactionId() + " retrieved");
        if (transactionMessage.getPhoneNumber().equals("+254716210475")) {
            MpesaCallbackMessage mpesaCallbackMessage = new MpesaCallbackMessage();
            mpesaCallbackMessage.setMerchantRequestID("29115-34620561-1");
            mpesaCallbackMessage.setTransactionID(transactionMessage.getTransactionId());
            mpesaCallbackMessage.setCheckoutRequestID("ws_CO_191220191020363925");
            mpesaCallbackMessage.setTransactionAmount(transactionMessage.getAmount());
            mpesaCallbackMessage.setResultCode(0);
            mpesaCallbackMessage.setResultDesc("The service request is processed successfully.");

            System.out.println("Callback is:  " + mpesaCallbackMessage);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.CALLBACK_QUEUE, mpesaCallbackMessage);
        } else {
            MpesaCallbackMessage mpesaCallbackMessage = new MpesaCallbackMessage();
            mpesaCallbackMessage.setMerchantRequestID("29115-34620561-1");
            mpesaCallbackMessage.setTransactionID(transactionMessage.getTransactionId());
            mpesaCallbackMessage.setCheckoutRequestID("ws_CO_191220191020363925");
            mpesaCallbackMessage.setTransactionAmount(transactionMessage.getAmount());
            mpesaCallbackMessage.setResultCode(1023);
            mpesaCallbackMessage.setResultDesc("The User canceled the request");

            System.out.println("Callback is:  " + mpesaCallbackMessage);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.CALLBACK_QUEUE, mpesaCallbackMessage);
        }
    }
}

package com.openhub.mpesasimulatordemo.services;

import com.openhub.mpesasimulatordemo.Utilities.RabbitMQConfig;
import com.openhub.mpesasimulatordemo.models.StkCallbackMessage;
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
            StkCallbackMessage stkCallbackMessage = new StkCallbackMessage().createCallbackMessage(
                    "29115-34620561-1",
                    "ws_CO_191220191020363925",
                    0,
                    "The service request is processed successfully.",
                    0.0,
                    transactionMessage.getPhoneNumber(),
                    "",
                    ""
            );

            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.CALLBACK_QUEUE, stkCallbackMessage);
        } else {
            StkCallbackMessage stkCallbackMessage = new StkCallbackMessage().createCallbackMessage(
                    "29115-34620561-1",
                    "ws_CO_191220191020363925",
                    0,
                    "The service request is processed successfully.",
                    0.0,
                    transactionMessage.getPhoneNumber(),
                    "",
                    ""
            );
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.CALLBACK_QUEUE, stkCallbackMessage);
        }
    }
}

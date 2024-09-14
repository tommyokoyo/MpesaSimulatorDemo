package com.openhub.mpesasimulatordemo.services;

import com.openhub.mpesasimulatordemo.Utilities.Generator;
import com.openhub.mpesasimulatordemo.Utilities.RabbitMQConfig;
import com.openhub.mpesasimulatordemo.models.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MpesaServices {
    private final RabbitTemplate rabbitTemplate;

    public MpesaServices(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void lipaNaMpesaDemo(MpesaExpressRequest mpesaExpressRequest, StkResponse stkResponse) {
        StkCallbackMessage stkCallbackMessage = new StkCallbackMessage().createCallbackMessage(
                stkResponse.getMerchantRequestID(),
                stkResponse.getCheckoutRequestID(),
                ResponseCode.CANCELED_BY_USER.getResponseCode(),
                ResponseMessage.FAILED.getMessage(),
                Double.valueOf(mpesaExpressRequest.getAmount()),
                mpesaExpressRequest.getPhoneNumber(),
                String.valueOf(LocalDateTime.now()),
                Generator.transactionRefGenerator()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.CALLBACK_QUEUE, stkCallbackMessage);
    }
}

package com.openhub.mpesasimulatordemo.services;

import com.openhub.mpesasimulatordemo.Utilities.GeneratorComponent;
import com.openhub.mpesasimulatordemo.Utilities.RabbitMQConfig;
import com.openhub.mpesasimulatordemo.models.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MpesaServices {
    private final GeneratorComponent generatorComponent;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MpesaServices(RabbitTemplate rabbitTemplate, GeneratorComponent generatorComponent) {
        this.rabbitTemplate = rabbitTemplate;
        this.generatorComponent = generatorComponent;
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
                generatorComponent.transactionRefGenerator()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.CALLBACK_QUEUE, stkCallbackMessage);
    }
}

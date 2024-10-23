package com.openhub.mpesasimulatordemo.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class provides configuration methods for RabbitMQ.
 * <p>
 *
 * @author Thomas Okoyo
 * @version 1.0
 * @since 2024
 */
@Configuration
public class RabbitMQConfiguration {
    public static final String TRANSACTION_QUEUE = "MsimTransactionQueue";
    public static final String CALLBACK_QUEUE = "MsimCallbackQueue";
    public static final String TRANSACTION_EXCHANGE = "MsimTransactionExchange";

    /**
     * This bean defines the Transaction Queue
     *
     * @return Queue
     */
    @Bean
    public Queue transactionQueue() {
        return new Queue(TRANSACTION_QUEUE, false);
    }

    /**
     * This bean defines the Callback Queue
     *
     * @return Queue
     */
    @Bean
    public Queue callBackQueue() {
        return new Queue(CALLBACK_QUEUE, false);
    }

    /**
     * This bean the Topic Exchange
     * <p>
     * The exchange routes messages based on routing key pattern
     *
     * @return exchange
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TRANSACTION_EXCHANGE);
    }

    /**
     * This bean binds the transactionQueue to the Topic Exchange
     *
     * @return exchange
     */
    @Bean
    public Binding transactionBinding(Queue transactionQueue, TopicExchange exchange) {
        return BindingBuilder.bind(transactionQueue).to(exchange).with(TRANSACTION_EXCHANGE);
    }

    /**
     * This bean binds the callBackQueue to the Topic Exchange
     *
     * @return exchange
     */
    @Bean
    public Binding callBackBinding(Queue callBackQueue, TopicExchange exchange) {
        return BindingBuilder.bind(callBackQueue).to(exchange).with(CALLBACK_QUEUE);
    }

    /**
     * This bean sets up a message converter
     *
     * @return Jackson2JsonMessageConverter
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * This bean is used to send and receive messages with RabbitMQ
     *
     * @param connectionFactory responsible for connectivity
     * @return RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}

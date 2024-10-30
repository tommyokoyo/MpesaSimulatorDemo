package com.openhub.mpesasimulatordemo.services;

import com.openhub.mpesasimulatordemo.entities.CallBackMessage;
import com.openhub.mpesasimulatordemo.entities.TransactionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Service
public class QueueService {
    private final BlockingQueue<TransactionMessage> transactionQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<CallBackMessage> callbackQueue = new LinkedBlockingQueue<>();

    private static final String TRANSACTION_QUEUE = "transactionQueue";

    public void addTransactionToQueue(String queueName, TransactionMessage transactionMessage) throws IllegalArgumentException {
        if (TRANSACTION_QUEUE.equals(queueName)) {
            if (transactionQueue.offer(transactionMessage)) {
                log.info("Transaction with MerchantRequestID {} added to Transaction Queue", transactionMessage.getMerchantRequestID());
            } else {
                log.warn("Transaction with MerchantRequestID {} failed to added to Transaction Queue", transactionMessage.getMerchantRequestID());
            }
        } else {
            log.error("Invalid Queue name for Transaction with MerchantRequestID {}", transactionMessage.getMerchantRequestID());
            throw new IllegalArgumentException("Invalid queue name: " + queueName);
        }
    }

    public void addCallbackToQueue(String queueName, CallBackMessage callBackMessage) throws IllegalArgumentException {
        if ("callbackQueue".equals(queueName)) {
            if (callbackQueue.offer(callBackMessage)) {
                log.info("Transaction with MerchantRequestID {} added to callback queue", callBackMessage.getMerchantRequestID());
            } else {
                log.warn("Failed to add Callback Message to queue");
            }
        } else {
            throw new IllegalArgumentException("Invalid queue name: " + queueName);
        }
    }

    public TransactionMessage getTransactionFromQueue(String queueName) throws IllegalArgumentException {
        if (TRANSACTION_QUEUE.equals(queueName)) {
            return transactionQueue.poll();
        } else {
            throw new IllegalArgumentException("Invalid queue name: " + queueName);
        }
    }

    public CallBackMessage getCallbackFromQueue(String queueName) throws IllegalArgumentException {
        if ("callbackQueue".equals(queueName)) {
            return callbackQueue.poll();
        } else {
            throw new IllegalArgumentException("Invalid queue name: " + queueName);
        }
    }

    public boolean checkQueue(String queueName) throws IllegalArgumentException {
        if ("transactionQueue".equals(queueName)) {
            return transactionQueue.isEmpty();
        } else if ("callbackQueue".equals(queueName)) {
            return callbackQueue.isEmpty();
        } else {
            throw new IllegalArgumentException("Invalid queue name: " + queueName);
        }
    }
}

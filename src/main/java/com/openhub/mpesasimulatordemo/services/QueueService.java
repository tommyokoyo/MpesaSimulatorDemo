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

    public void addTransactionToQueue(String queueName, TransactionMessage transactionMessage) throws IllegalArgumentException {
        if ("transactionQueue".equals(queueName)) {
            if (transactionQueue.offer(transactionMessage)) {
                log.info("Transaction Message added to queue");
            } else {
                log.warn("Transaction Message not added to queue");
            }
        } else {
            throw new IllegalArgumentException("Invalid queue name: " + queueName);
        }
    }

    public void addCallbackToQueue(String queueName, CallBackMessage callBackMessage) throws IllegalArgumentException {
        if ("callbackQueue".equals(queueName)) {
            if (callbackQueue.offer(callBackMessage)) {
                log.info("Callback Message added to queue");
            } else {
                log.warn("Failed to add Callback Message to queue");
            }
        } else {
            throw new IllegalArgumentException("Invalid queue name: " + queueName);
        }
    }

    public TransactionMessage getTransactionFromQueue(String queueName) throws IllegalArgumentException {
        if ("transactionQueue".equals(queueName)) {
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

    public boolean isQueueEmpty(String queueName) throws IllegalArgumentException {
        if ("transactionQueue".equals(queueName)) {
            return transactionQueue.isEmpty();
        } else if ("callbackQueue".equals(queueName)) {
            return callbackQueue.isEmpty();
        } else {
            throw new IllegalArgumentException("Invalid queue name: " + queueName);
        }
    }
}

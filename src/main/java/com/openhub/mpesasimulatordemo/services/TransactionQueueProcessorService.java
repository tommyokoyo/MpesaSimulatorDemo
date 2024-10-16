package com.openhub.mpesasimulatordemo.services;

import com.openhub.mpesasimulatordemo.Utilities.FileHandler;
import com.openhub.mpesasimulatordemo.Utilities.TransactionManager;
import com.openhub.mpesasimulatordemo.models.TransactionMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class TransactionQueueProcessorService {
    private final TransactionManager transactionManager;
    private final BlockingQueue<TransactionMessage> transactionQueue;

    @Autowired
    public TransactionQueueProcessorService(TransactionManager transactionManager) {
        this.transactionQueue = new LinkedBlockingQueue<>();
        this.transactionManager = transactionManager;
    }

    @PostConstruct
    public void startQueueProcessor() {
        new Thread(() -> {
            try {
                TransactionMessage transactionMessage = transactionQueue.take();
                processTransaction(transactionMessage);
            } catch (InterruptedException e) {
                System.out.println("Error while starting processing queue: " + e.getMessage());
            }
        }).start();
    }

    private void processTransaction(TransactionMessage transactionMessage) {
        System.out.println("[-] Processing transaction simulation for " + transactionMessage.getTransactionId());
        try {
            transactionManager.addTransactionMessage(transactionMessage);
        } catch (IOException e) {
            System.out.println("[-] Error while processing transaction: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public void addTransaction(TransactionMessage transactionMessage) {
        try {
            transactionQueue.put(transactionMessage);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Error while adding transaction: " + e.getMessage());
        }
    }
}

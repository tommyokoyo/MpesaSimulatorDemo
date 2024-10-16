package com.openhub.mpesasimulatordemo.Utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openhub.mpesasimulatordemo.models.TransactionMessage;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TransactionManager {
    private final String SUCCESS_TRANSACTION = "Success.txt";
    private final String FAILED_TRANSACTION = "Failed.json";
    private final String PROCESSING_TRANSACTION = "Processing.json";

    private final ObjectMapper objectMapper;
    private final List<TransactionMessage> transactionMessages;
    private final Map<String, TransactionMessage> transactionsMap;

    public TransactionManager() throws IOException {
        this.objectMapper = new ObjectMapper();
        this.transactionMessages = new ArrayList<>();
        this.transactionsMap = new HashMap<>();
        loadTransactions();
    }

    public void addTransactionMessage(TransactionMessage transactionMessage) throws IOException {
        this.transactionMessages.add(transactionMessage);
        this.transactionsMap.put(transactionMessage.getTransactionId(), transactionMessage);
        saveTransactions();
    }

    private void saveTransactions() throws IOException {
        objectMapper.writeValue(new File("transactions.json"), transactionsMap);
    }

    private void loadTransactions() throws IOException {
        File file = new File("transactions.json");
        if (file.exists()) {
            transactionMessages.addAll(objectMapper.readValue(file, new TypeReference<List<TransactionMessage>>() {}));
            for (TransactionMessage transactionMessage : transactionMessages) {
                transactionsMap.put(transactionMessage.getTransactionId(), transactionMessage);
            }
        }
    }

    public TransactionMessage getTransaction(String transactionId) {
        return transactionsMap.get(transactionId);
    }
}

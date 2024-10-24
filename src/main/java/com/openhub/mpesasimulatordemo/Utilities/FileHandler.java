package com.openhub.mpesasimulatordemo.Utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openhub.mpesasimulatordemo.entities.TransactionMessage;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Component
public class FileHandler {
    private final String SESSIONTOKENS = "token.txt";
    private final String JSONTOKENS = "token.json";
    private final String TRANSACTIONS = "transactions.json";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, String> loadCredentials(String fileName) throws IOException {
        Map<String, String> credentials = new HashMap<>();
        String USERCREDENTIALS = "credentials.txt";
        BufferedReader credentialReader = new BufferedReader(new FileReader(USERCREDENTIALS));
        String line;

         while ((line = credentialReader.readLine()) != null) {
             String[] credentialArray = line.split(":");
             if (credentialArray.length == 2) {
                 credentials.put(credentialArray[0], credentialArray[1]);
             }
         }

         credentialReader.close();
         return credentials;
    }

    public List<String> readTokens() throws IOException {
        return Files.readAllLines(Paths.get(SESSIONTOKENS));
    }

    public Set<String> readJSONTokens() throws IOException {
        File file = new File(JSONTOKENS);

        if (file.exists() && file.length() > 0) {
            return objectMapper.readValue(new File(JSONTOKENS),
                    new TypeReference<HashSet<String>>() {});
        } else {
            return new HashSet<>();
        }
    }

    public void saveToken(String token) throws IOException {
        Set<String> tokens = readJSONTokens();
        tokens.add(token);

        objectMapper.writeValue(new File(JSONTOKENS), tokens);

        BufferedWriter credentialWriter = new BufferedWriter(new FileWriter(SESSIONTOKENS));
        credentialWriter.write(token);
        credentialWriter.newLine();
        credentialWriter.close();
    }

    public ArrayList<TransactionMessage> readTransactions() throws IOException {
        File file = new File(TRANSACTIONS);
        if (file.exists() && file.length() > 0) {
            return objectMapper.readValue(new File(TRANSACTIONS), new TypeReference<ArrayList<TransactionMessage>>() {});
        }
        return null;
    }

    public void saveTransaction(TransactionMessage transaction) throws IOException {

    }
}

package com.openhub.mpesasimulatordemo.Utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Component
public class FileHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String USERCREDENTIALS = "credentials.txt";
    private String SESSIONTOKENS = "token.txt";
    private String JSONTOKENS = "token.json";

    public Map<String, String> loadCredentials(String fileName) throws IOException {
        Map<String, String> credentials = new HashMap<>();
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
}

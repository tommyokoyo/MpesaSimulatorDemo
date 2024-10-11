package com.openhub.mpesasimulatordemo.Utilities;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FileHandler {
    private final String USERCREDENTIALS = "credentials.txt";
    private String SESSIONTOKENS = "token.txt";

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

    public void saveToken(String token) throws IOException {
        BufferedWriter credentialWriter = new BufferedWriter(new FileWriter(SESSIONTOKENS));
        credentialWriter.write(token);
        credentialWriter.newLine();
        credentialWriter.close();
    }
}

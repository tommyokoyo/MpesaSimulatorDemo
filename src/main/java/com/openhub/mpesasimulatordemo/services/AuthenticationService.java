package com.openhub.mpesasimulatordemo.services;

import com.openhub.mpesasimulatordemo.Utilities.FileHandler;
import com.openhub.mpesasimulatordemo.Utilities.GeneratorComponent;
import com.openhub.mpesasimulatordemo.models.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final GeneratorComponent generatorComponent;
    private final FileHandler fileHandler;

    @Autowired
    public AuthenticationService(GeneratorComponent generatorComponent, FileHandler fileHandler) {
        this.generatorComponent = generatorComponent;
        this.fileHandler = fileHandler;
    }

    public ResponseEntity<?> authenticateUser(String username, String password) {
        if (username.equals("okoyo") && password.equals("password")) {
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            String accessToken = generatorComponent.tokenGenerator();
            authenticationResponse.setAccess_token(accessToken);
            try {
                fileHandler.saveToken(accessToken);
                return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
            } catch (Exception e) {
                System.out.println("Token produced: " + accessToken);
                System.out.println("Failed to write Token to file" + e.getMessage());
                return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

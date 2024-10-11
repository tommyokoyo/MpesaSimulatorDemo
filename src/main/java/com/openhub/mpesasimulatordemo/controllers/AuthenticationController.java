package com.openhub.mpesasimulatordemo.controllers;

import com.openhub.mpesasimulatordemo.models.AuthenticationResponse;
import com.openhub.mpesasimulatordemo.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/mpesa-sim/authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/get-token")
    public ResponseEntity<?> getAuthenticationToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            String authCredentials = authorizationHeader.substring("Basic ".length());

            byte[] decodedBytes = Base64.getDecoder().decode(authCredentials);
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

            String[] tokens = decodedString.split(":");
            if (tokens.length == 2) {
                String username = tokens[0];
                String password = tokens[1];

                return authenticationService.authenticateUser(username, password);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

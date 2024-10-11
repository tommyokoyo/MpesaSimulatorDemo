package com.openhub.mpesasimulatordemo.Utilities;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class GeneratorComponent {
    public String tokenGenerator() {
        return UUID.randomUUID().toString();
    }

    public String MerchantIDGenerator() {
        String serviceCode = "MPESA_SIM";
        // Generate five-digit number
        int segOne = ThreadLocalRandom.current().nextInt(10000, 99999 + 1);
        int segTwo = ThreadLocalRandom.current().nextInt(1000000, 9999999  + 1);
        int segThree = ThreadLocalRandom.current().nextInt(1, 9 + 1);

        return serviceCode + "-" + segOne + "-" + segTwo + "-" + segThree;
    }

    public String CheckoutRequestIDGenerator() {
        String serviceCode = "MPESA_SIM";
        int segOne = ThreadLocalRandom.current().nextInt(10000, 99999 + 1);
        int segTwo = ThreadLocalRandom.current().nextInt(1000000, 9999999  + 1);

        return serviceCode + "_" + segOne + "_" + segTwo;
    }

    public String transactionRefGenerator() {
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder(10);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            builder.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return "MSIM_" + builder;
    }
}

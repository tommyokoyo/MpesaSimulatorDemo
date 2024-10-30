package com.openhub.mpesasimulatordemo.components;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.openhub.mpesasimulatordemo.Utilities.GeneratorComponent;
import com.openhub.mpesasimulatordemo.models.MsimCallbackMessage;
import com.openhub.mpesasimulatordemo.models.MsimStkRequest;
import com.openhub.mpesasimulatordemo.models.MsimStkResponse;
import com.openhub.mpesasimulatordemo.repository.TransactionRepository;
import com.openhub.mpesasimulatordemo.services.QueueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MessageHandlerTest {
    @Mock
    private GeneratorComponent generatorComponent;

    @Mock
    private QueueService queueService;

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    private MessageHandler messageHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStkErrorResponse(){
        when(generatorComponent.MerchantIDGenerator()).thenReturn("mockMerchantRequestID");
        when(generatorComponent.CheckoutRequestIDGenerator()).thenReturn("mockCheckoutRequestID");

        MsimStkResponse response = new MsimStkResponse();

        assertEquals("mockMerchantRequestID", response.getMerchantRequestID());
        assertEquals("mockCheckoutRequestID", response.getCheckoutRequestID());
        assertEquals("1", response.getResponseCode());
        assertEquals("The service request has failed", response.getResponseDescription());
        assertEquals("Error. Could not process request", response.getCustomerMessage());
    }

    @Test
    void testHandleSuccessfulTransaction(){
        MsimStkRequest request = new MsimStkRequest();
        request.setBusinessShortCode("174379");
        request.setPassword("MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMTYwMjE2MTY1NjI3MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMTYwMjE2MTY1NjI3");
        request.setTimestamp("123456789");
        request.setTransactionType("CustomerPayBillOnline");
        request.setAmount("100");
        request.setPartyA("+254716210475");
        request.setPartyB("+254113417176");
        request.setPhoneNumber("+254113417175");
        request.setCallBackURL("https://localhost:9090/api/transaction/stk-callback");
        request.setAccountReference("Test_account");
        request.setTransactionDesc("Test_account");
    }
}

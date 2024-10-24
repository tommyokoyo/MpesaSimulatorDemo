package com.openhub.mpesasimulatordemo.services;

import com.openhub.mpesasimulatordemo.models.MsimStkCallbackResponse;
import com.openhub.mpesasimulatordemo.models.MsimStkCallbackRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * This service class provides the WebClient instance for sending callbacks
 *
 * @author Thomas Okoyo
 * @version 1.0
 */
@Service
public class CallbackService {
    private final WebClient.Builder webClientBuilder;

    public CallbackService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * This class build the WebClient instance using the provided url
     *
     * @param url url to send Callback response
     * @return WebClient instance
     */
    private WebClient getWebClient(String url) {
        return webClientBuilder.baseUrl(url).build();
    }

    /**
     * This class makes send the POST request contain the callback response
     * <p>
     * It makes use of the resilience retry mechanisms for retry in case of failure
     * combined with a circuit breaker that prevents overloading the client with requests
     * in case the client is offline
     *
     * @param msimStkCallbackRequest requests to be sent
     * @param url url to send the request
     * @return MsimStkCallbackResponse object contain the response
     */
    @Retry(name = "StkCallbackRequest", fallbackMethod = "fallBackMpesaRequest")
    @CircuitBreaker(name = "StkCallBackCircuitBreaker", fallbackMethod = "fallBackMpesaRequest")
    public Mono<MsimStkCallbackResponse> stkCallback(MsimStkCallbackRequest msimStkCallbackRequest, String url) {
        WebClient webClient = getWebClient(url);
        return webClient.post()
                .bodyValue(msimStkCallbackRequest)
                .retrieve()
                .bodyToMono(MsimStkCallbackResponse.class)
                .onErrorResume(WebClientResponseException.class, this::handleWebclientException);
    }

    /**
     * This method handles exceptions from the Webclient
     *
     * @param e exception
     * @return Modified response
     */
    private Mono<MsimStkCallbackResponse> handleWebclientException(WebClientResponseException e) {
        MsimStkCallbackResponse msimStkCallbackResponse = e.getResponseBodyAs(MsimStkCallbackResponse.class);
        if (msimStkCallbackResponse != null) {
            return Mono.just(msimStkCallbackResponse);
        } else {
            MsimStkCallbackResponse errorResponse = new MsimStkCallbackResponse();
            System.out.println("Ërror response Body: " + errorResponse);
            errorResponse.setStatus("1");
            errorResponse.setMessage("Service Error");
            return Mono.just(errorResponse);
        }
    }

    /**
     * This is the fallback method in case the retries fail
     *
     * @param RequestData payload to be sent
     * @param throwable throwable
     * @return modified error response
     */
    public Mono<String> fallBackMpesaRequest(String RequestData, Throwable throwable) {
        return Mono.just("M-SIM service not available:");
    }
}

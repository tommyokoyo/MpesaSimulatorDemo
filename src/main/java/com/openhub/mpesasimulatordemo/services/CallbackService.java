package com.openhub.mpesasimulatordemo.services;

import com.openhub.mpesasimulatordemo.models.CallbackResponse;
import com.openhub.mpesasimulatordemo.models.MpesaExpressRequest;
import com.openhub.mpesasimulatordemo.models.StkCallbackMessage;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class CallbackService {
    private final WebClient.Builder webClientBuilder;

    public CallbackService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    private WebClient getWebClient(String url) {
        return webClientBuilder.baseUrl(url).build();
    }

    @Retry(name = "StkCallbackRequest", fallbackMethod = "fallBackMpesaRequest")
    @CircuitBreaker(name = "StkCallBackCircuitBreaker", fallbackMethod = "fallBackMpesaRequest")
    public Mono<CallbackResponse> stkCallback(StkCallbackMessage stkCallbackMessage, String url) {
        WebClient webClient = getWebClient(url);
        return webClient.post()
                .bodyValue(stkCallbackMessage)
                .retrieve()
                .bodyToMono(CallbackResponse.class)
                .onErrorResume(WebClientResponseException.class, this::handleWebclientException);
    }

    private Mono<CallbackResponse> handleWebclientException(WebClientResponseException e) {
        CallbackResponse callbackResponse = e.getResponseBodyAs(CallbackResponse.class);
        if (callbackResponse != null) {
            return Mono.just(callbackResponse);
        } else {
            CallbackResponse errorResponse = new CallbackResponse();
            System.out.println("Ërror response Body: " + errorResponse);
            errorResponse.setStatus("1");
            errorResponse.setMessage("Service Error");
            return Mono.just(errorResponse);
        }
    }

    public Mono<String> fallBackMpesaRequest(String RequestData, Throwable throwable) {
        return Mono.just("M-SIM service not available:");
    }
}

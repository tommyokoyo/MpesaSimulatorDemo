package com.openhub.mpesasimulatordemo.services;

import com.openhub.mpesasimulatordemo.models.MsimStkCallbackResponse;
import com.openhub.mpesasimulatordemo.models.MsimStkCallbackRequest;
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
    public Mono<MsimStkCallbackResponse> stkCallback(MsimStkCallbackRequest msimStkCallbackRequest, String url) {
        WebClient webClient = getWebClient(url);
        return webClient.post()
                .bodyValue(msimStkCallbackRequest)
                .retrieve()
                .bodyToMono(MsimStkCallbackResponse.class)
                .onErrorResume(WebClientResponseException.class, this::handleWebclientException);
    }

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

    public Mono<String> fallBackMpesaRequest(String RequestData, Throwable throwable) {
        return Mono.just("M-SIM service not available:");
    }
}

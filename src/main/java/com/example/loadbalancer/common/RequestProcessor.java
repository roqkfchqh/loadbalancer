package com.example.loadbalancer.common;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class RequestProcessor {

    private final LoadBalancer loadBalancer;
    private final WebClient webClient;

    public RequestProcessor(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
        this.webClient = WebClient.create();
    }

    public Mono<String> processRequest(String request){
        try {
            String serverUrl = loadBalancer.getNextServer();

            return webClient.post()
                .uri(serverUrl)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.just("Error: " + e.getMessage()));
        }catch(Exception e){
            return Mono.just("Error: " + e.getMessage());
        }
    }
}
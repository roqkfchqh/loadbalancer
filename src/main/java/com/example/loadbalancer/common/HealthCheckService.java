package com.example.loadbalancer.common;

import com.example.loadbalancer.values.LoadBalancerConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class HealthCheckService {

    private final LoadBalancerConfigProperties configProperties;
    private final CopyOnWriteArrayList<String> healthyServers = new CopyOnWriteArrayList<>();
    private final WebClient webClient = WebClient.builder().build();

    public List<String> getHealthyServers() {
        return List.copyOf(healthyServers);
    }

    @Scheduled(fixedRateString = "${healthcheck.fixedRate}")
    public void performHealthCheck() {
        List<String> allServers = configProperties.servers();
        log.info("Health check for servers: {}", allServers);
        allServers.forEach(server ->
                isServerHealthy(server).subscribe(isHealthy -> {
                    if (isHealthy) {
                        if (!healthyServers.contains(server)) {
                            healthyServers.add(server);
                        }
                    } else {
                        healthyServers.remove(server);
                    }
                })
        );
        log.info("Health check finished.");
        log.info("Updated healthy servers: {}", healthyServers);
    }

    public Mono<Integer> getResponseTime(String serverUrl) {
        long startTime = System.currentTimeMillis();
        return webClient.get()
                .uri(serverUrl + "/actuator/health")
                .retrieve()
                .toBodilessEntity()
                .map(response -> (int) (System.currentTimeMillis() - startTime))
                .onErrorReturn(configProperties.maxResponseTime());
    }

    private Mono<Boolean> isServerHealthy(String serverUrl) {
        return webClient.get()
                .uri(serverUrl + "/actuator/health")
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .onErrorReturn(false);
    }
}

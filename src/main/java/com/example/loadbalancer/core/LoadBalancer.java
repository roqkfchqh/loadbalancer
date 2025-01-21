package com.example.loadbalancer.core;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Slf4j
@Component
public class LoadBalancer {

    private final HealthCheckService healthCheckService;
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    public LoadBalancer(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    //다음 정상서버를 가져오고 클라이언트 요청 전달 준비
    public String forwardRequest(String clientRequest) throws Exception {
        log.info("Received client request: {}", clientRequest);
        String server = getNextServer();

        //클라이언트 요청을 안전하게 인코딩
        String encodedRequest = java.net.URLEncoder.encode(clientRequest, StandardCharsets.UTF_8);
        log.info("Encoded request: {}", encodedRequest);
        //get 요청 준비
        URL url = new URL(server + "/" + encodedRequest);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        //서버의 응답데이터를 읽어와서 문자열로 반환
        //try-with-resources 를 사용해 자원을 자동으로 닫음
        try (var in = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            log.info("Request forwarded to {} with response: {}", server, response);
            return response.toString();
        }
    }

    //현재 정상서버 리스트 가져옴
    private String getNextServer() {
        List<String> healthyServers = healthCheckService.getHealthyServers();

        //정상서버가 하나도 없는경우
        if (healthyServers.isEmpty()) {
            throw new IllegalStateException("No healthy servers available.");
        }

        //라운드로빈 방식으로 서버 순회 -> 다음 서버 선택
        //인덱스는 리스트 크기를 초과하지 않도록 순환됨
        int index = currentIndex.getAndUpdate(i -> (i + 1) % healthyServers.size());
        return healthyServers.get(index);
    }
}

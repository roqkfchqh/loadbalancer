package com.example.loadbalancer.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class HealthCheckService {

    //healthCheck 대상 서버 url 리스트
    private final List<String> servers = List.of(
        "http://localhost:8081",
        "http://localhost:8082",
        "http://localhost:8083"
    );
    //정상 서버들을 저장하는 스레드 안전 리스트
    private final CopyOnWriteArrayList<String> healthyServers = new CopyOnWriteArrayList<>();

    //정상 서버들을 읽기 전용으로 외부에 제공
    public List<String> getHealthyServers() {
        return List.copyOf(healthyServers); //변경 불가능한 리스트 반환
    }

    @Scheduled(fixedRate = 5000) //healthCheck 5초마다 실행
    public void performHealthCheck() {
        //정상 서버 리스트 저장용
        CopyOnWriteArrayList<String> updatedHealthyServers = new CopyOnWriteArrayList<>();

        //모든 서버를 순회하며, 정상 서버를 리스트에 추가
        for (String server : servers) {
            if (isServerHealthy(server)) {
                updatedHealthyServers.add(server);
            }
        }

        //기존 정상서버 리스트 지우고, 새로운 리스트 갱신
        healthyServers.clear();
        healthyServers.addAll(updatedHealthyServers);

        //현재 정상서버 리스트 출력
        log.info("Healthy servers: {}", healthyServers);
    }

    //개별서버에 요청을 보내 상태 확인
    private boolean isServerHealthy(String serverUrl) {
        try {
            //http get 요청을 보내고
            URL url = new URL(serverUrl + "/actuator/health");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //2초 이내에 200 응답 반환하면 정상으로 인식
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            return connection.getResponseCode() == 200;
        } catch (Exception e) {
            //예외 시 해당 서버를 비정상으로 간주하고 에러 출력
            log.error("Health check failed for {}: {}", serverUrl, e.getMessage());
            return false;
        }
    }
}

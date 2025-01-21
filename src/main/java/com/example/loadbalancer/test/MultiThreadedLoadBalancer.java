//package com.example.loadbalancer.test;
//
//import com.example.loadbalancer.core.LoadBalancer;
//import jakarta.annotation.PostConstruct;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class MultiThreadedLoadBalancer {
//
//    private final LoadBalancer loadBalancer;
//
//    public MultiThreadedLoadBalancer(LoadBalancer loadBalancer) {
//        this.loadBalancer = loadBalancer;
//    }
//
//    //멀티스레드 환경에서의 로드밸런서 테스트용
//    @PostConstruct
//    public void runTest() {
//        //10개의 스레드를 가진 고정 스레드풀 생성
//        ExecutorService executor = Executors.newFixedThreadPool(10);
//
//        //10개의 요청을 비동기적으로 실행
//        for (int i = 0; i < 10; i++) {
//            //각 요청마다 고유한 id 할당해 구분
//            final int requestId = i + 1;
//            executor.execute(() -> {
//                //클라이언트 요청을 로드밸런서를 통해 서버로 전달, 응답결과 출력
//                try {
//                    String response = loadBalancer.forwardRequest("/api/example");
//                    log.info("Request {} Response: {}", requestId, response);
//                } catch (Exception e) {
//                    log.error("Request {} failed: {}", requestId, e.getMessage());
//                }
//            });
//        }
//
//        //작업 완료 시 스레드풀 종료
//        executor.shutdown();
//        try {
//            // 최대 30초 동안 모든 작업 완료 대기
//            boolean terminated = executor.awaitTermination(30, TimeUnit.SECONDS);
//            if (!terminated) {
//                // 작업이 제한 시간 내에 종료되지 않은 경우 처리
//                log.error("Executor did not terminate in the specified time.");
//                executor.shutdownNow(); // 강제 종료
//            } else {
//                log.info("All tasks completed successfully.");
//            }
//        } catch (InterruptedException e) {
//            log.error("Thread interrupted while waiting for termination.", e);
//            executor.shutdownNow(); // 인터럽트 시 강제 종료
//            Thread.currentThread().interrupt(); // 현재 스레드의 인터럽트 상태 복원
//        }
//    }
//}

package com.example.loadbalancer.concrete;

import com.example.loadbalancer.common.LoadBalancerStrategy;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinAtomic implements LoadBalancerStrategy {

    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public String getNextServer(List<String> healthyServers) {

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

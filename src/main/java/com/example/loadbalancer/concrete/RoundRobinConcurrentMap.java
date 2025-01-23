package com.example.loadbalancer.concrete;

import com.example.loadbalancer.common.LoadBalancerStrategy;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RoundRobinConcurrentMap implements LoadBalancerStrategy {

    private final ConcurrentHashMap<Thread, Integer> threadIndex = new ConcurrentHashMap<>();

    @Override
    public String getNextServer(List<String> healthyServers) {
        if (healthyServers.isEmpty()) {
            throw new IllegalStateException("No healthy servers available.");
        }
        int index = threadIndex.compute(Thread.currentThread(), (thread, i) -> (i == null ? 0 : (i + 1) % healthyServers.size()));
        return healthyServers.get(index);
    }
}


package com.example.loadbalancer.concrete;

import com.example.loadbalancer.common.LoadBalancerStrategy;
import java.util.List;

public class RoundRobinThreadLocal implements LoadBalancerStrategy {
    private final ThreadLocal<Integer> threadLocalIndex = ThreadLocal.withInitial(() -> 0);

    @Override
    public String getNextServer(List<String> healthyServers) {
        if (healthyServers.isEmpty()) {
            throw new IllegalStateException("No healthy servers available.");
        }
        int index = threadLocalIndex.get();
        threadLocalIndex.set((index + 1) % healthyServers.size());
        return healthyServers.get(index);
    }
}

